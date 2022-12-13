create user whistle@'%' identified by 'P@ssw0rd';

create database if not exists whistle;

grant all privileges on whistle.* to whistle@'%';

use whistle;


create table if not exists operate_log_info
(
    id                      int auto_increment comment '主键'
        primary key,
    uid                     int           default 0                 not null comment '操作用户id',
    uname                   varchar(64)   default ''                not null comment '操作用户名',
    from_ip                 varchar(64)   default ''                not null comment '操作人的机器ip',
    operate_func            varchar(30)   default ''                not null comment '操作的功能，例如：登录、插入记录等等',
    visit_method            varchar(200)  default ''                null comment '调用入口的方法',
    method_cost_time        varchar(10)   default ''                not null comment '访问方法所花费的时间',
    log_type                varchar(32)   default ''                not null comment '日志类型:error/info/warn',
    uri                     varchar(200)  default ''                not null comment '访问路径',
    method                  varchar(16)   default ''                not null comment '请求方法:post/get/put/delete/head',
    visit_method_error_info varchar(1000) default ''                not null comment '访问方法的错误信息',
    operated_time           timestamp     default CURRENT_TIMESTAMP not null comment '操作时间'
)
    comment '操作日志表';

create table if not exists pri_rbac_privileges
(
    pri_id       int auto_increment comment '权限id'
        primary key,
    pid          int                                    not null comment '父权限id',
    title        varchar(64)  default ''                not null comment '权限标题',
    uri          varchar(200) default ''                not null comment '权限路径',
    type         char         default 'M'               not null comment '权限类型 M-菜单、T-侧边栏、B-按钮（A-增、D-删、U-改、Q-查、I-导入、E-导出）',
    status       char         default '1'               not null comment '记录是否有效1-有效、0-无效，默认有效',
    created_by   varchar(30)  default 'admin'           not null comment '创建人',
    created_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_by   varchar(30)  default 'admin'           not null comment '更新人',
    updated_time datetime                               null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '权限表';

create table if not exists pri_rbac_role
(
    rid          int auto_increment comment '角色id'
        primary key,
    role_name    varchar(64) default ''                not null comment '角色名',
    role_desc    varchar(64) default ''                null comment '角色描述',
    pid          int                                   null comment '父角色',
    sid          int                                   null comment '子角色',
    mutex        int                                   null comment '互斥角色',
    max          int                                   null comment '最大角色限制',
    status       char        default '1'               not null comment '记录是否有效1-有效、0-无效，默认有效',
    created_by   varchar(30) default 'admin'           not null comment '创建人',
    created_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_by   varchar(30) default 'admin'           not null comment '更新人',
    updated_time datetime                              null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '角色表';

create table if not exists pri_rbac_role_privileges
(
    id           int auto_increment comment '主键'
        primary key,
    pri_id       int                                   not null comment '权限id',
    rid          int                                   not null comment '角色id',
    status       char        default '1'               not null comment '记录是否有效1-有效、0-无效，默认有效',
    created_by   varchar(30) default 'admin'           not null comment '创建人',
    created_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_by   varchar(30) default 'admin'           not null comment '更新人',
    updated_time datetime                              null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint pri_rbac_role_privileges_ibfk_1
        foreign key (rid) references pri_rbac_role (rid)
            on delete cascade,
    constraint pri_rbac_role_privileges_ibfk_2
        foreign key (pri_id) references pri_rbac_privileges (pri_id)
            on delete cascade
)
    comment '角色权限关联表';

create index pri_rbac_pri_privileges_relation
    on pri_rbac_role_privileges (pri_id);

create index pri_rbac_pri_role_relation
    on pri_rbac_role_privileges (rid);

create table if not exists pri_rbac_user
(
    uid          varchar(32)                           not null comment '用户唯一ID'
        primary key,
    user_name    varchar(30) default ''                not null comment '用户名',
    nick_name    varchar(30) default ''                null comment '昵称',
    password     varchar(36) default ''                not null comment '密码',
    gender       char        default ''                null comment '性别 0-男、1-女',
    age          varchar(20) default ''                null comment '年龄',
    birth        date                                  null comment '出生日期',
    id_card      varchar(18) default ''                null comment '身份证',
    address      varchar(60) default ''                null comment '联系地址',
    email        varchar(36) default ''                null comment '用户邮箱',
    mobile       varchar(11) default ''                null comment '手机号码',
    is_admin     char        default '0'               not null comment '是否为超级管理员1-是、0-否，默认不是',
    status       char        default '1'               not null comment '记录是否有效1-有效、0-无效，默认有效',
    created_by   varchar(30) default 'admin'           not null comment '创建人',
    created_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_by   varchar(30) default 'admin'           not null comment '更新人',
    updated_time datetime                              null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '权限用户登记表';

create table if not exists pri_rbac_user_role
(
    id           int auto_increment comment '主键'
        primary key,
    uid          varchar(32)                           not null comment '用户id',
    rid          int                                   not null comment '角色id',
    status       char        default '1'               not null comment '记录是否有效1-有效、0-无效，默认有效',
    created_by   varchar(30) default 'admin'           not null comment '创建人',
    created_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_by   varchar(30) default 'admin'           not null comment '更新人',
    updated_time datetime                              null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint pri_rbac_user_role_ibfk_1
        foreign key (uid) references pri_rbac_user (uid)
            on delete cascade,
    constraint pri_rbac_user_role_ibfk_2
        foreign key (rid) references pri_rbac_role (rid)
            on delete cascade
)
    comment '用户角色关联表';

create index pri_rbac_role_relation
    on pri_rbac_user_role (rid);

create index pri_rbac_user_relation
    on pri_rbac_user_role (uid);

create table if not exists some_sqls
(
    id       int auto_increment comment '主键'
        primary key,
    title    varchar(36) not null comment 'SQL作用描述',
    sql_text text        not null comment 'SQL'
)
    comment '一些常用SQL语句';

