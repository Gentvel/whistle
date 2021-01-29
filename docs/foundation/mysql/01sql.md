---
title: MySQL SQL基础
date: 2021-01-22
sidebar: auto
categories:
 - MySQL
tags:
- MySQL
prev: ./
next: false
---
## SQL分类
- DDL（Data Definition Languages）语句：数据定义语言，这些语句定义了不同的数据段、数据库、表、列、索引等数据库对象。常用的语句关键字主要包括create、drop、alter等。
- DML（Data Manipulation Language）语句：数据操纵语句，用于添加、删除、更新和查询数据库记录，并检查数据完整性。常用的语句关键字主要包括 insert、delete、update和select等。
- DCL（Data Control Language）语句：数据控制语句，用于控制不同数据段直接的许可和访问级别的语句。这些语句定义了数据库、表、字段、用户的访问权限和安全级别。主要的语句关键字包括grant、revoke等

## DDL语句
DDL是数据定义语言的缩写，简单来说，就是对数据库内部的对象进行创建、删除、修改等操作的语言。它和DML语句的最大区别是DML只是对表内部数据操作，而不涉及表的定义、结构的修改，更不会涉及其他对象。DDL 语句更多地由数据库管理员（DBA）使用，开发人员一般很少使用。

```sql
# 连接数据库
mysql -uroot -p
```
### 建表语句

1. 创建数据库
```sql
# 查看数据库中有哪些表
show databases;

#输出
information_schema # 主要存储了系统中的一些数据库对象信息，比如用户表信息、列信息、权限信息、字符集信息、分区信息等。
cluster # 存储了系统的集群信息。
mysql #存储了系统的用户权限信息。
performance_schema
sys


# 创建一个库
create database test;
# 选择数据库
use test;
#查看库的所有表
show tables;
```

2. 删除数据库

```sql
# 删除数据库
drop database test;
```

:::warning 注意
数据库删除后，下面的所有表数据都会全部删除，所以删除前一定要仔细检查并做好相应备份。
:::

3. 创建表  
在数据库中创建一张表的基本语法如下：
```sql
CREATE TABLE tablename (
    column_name_1 column_type_1 constraints,
    column_name_2 column_type_2 constraints,
    …
    column_name_n column_type_n constraints）


create table emp (
    id int not null primary key auto_increment comment 'id',
    name varchar(20) not null ,
    birth timestamp null default current_timestamp on update current_timestamp
)engine=InnoDB auto_increment=2 default charset = UTF8MB4

## 查看表结构
desc emp;

## 查看建表语句
show create table emp;
```

4. 删除表
```sql
drop table emp;
```

### 修改表语句

对于已经创建好的表，尤其是已经有大量数据的表，如果需要做一些结构上的改变，可以先将表删除（drop），然后再按照新的表定义重建表。这样做没有问题，但是必然要做一些额外的工作，比如数据的重新加载。而且，如果有服务在访问表，也会对服务产生影响。

```sql
#修改增加
ALTER TABLE tablename MODIFY|ADD [COLUMN] column_definition [FIRST |AFTER col_name]
#删除
ALTER TABLE tablename DROP  [COLUMN] col_name
#修改字段名
ALTER TABLE tablename CHANGE [COLUMN] old_col_name column_definition [FIRST |AFTER col_name]


# 1. 修改表字段类型
alter table emp modify name varchar(50) not null;
# 2. 添加表字段
alter table emp add age int(3) after name;
alter table emp add en_name varchar(100) after name;
# 3. 删除表字段
alter table emp drop en_name;
# 4. 修改表字段名和类型
alter table emp change age age1 varchar(3) null after name;
# 5. 修改字段，将它放在最前面
alter table emp change age1 age int(3) null first ;
# 6. 修改表名
alter table emp rename emp1;
```
:::warning
change和modify都可以修改表的定义，不同的是change后面需要写两次列名，不方便。但是change的优点是可以修改列名称，modify则不能。
:::

## DML语句

DML 操作是指对数据库中表记录的操作，主要包括表记录的插入（ insert ）、更新（update）、删除（delete）和查询（select），是开发人员日常使用最频繁的操作。

```sql
#在MySQL中，insert语句还有一个很好的特性，可以一次性插入多条记录，语法如下：
INSERT INTO tablename (field1, field2, …, fieldn)
VALUES
(record1_value1, record1_value2, …, record1_valuesn),
(record2_value1, record2_value2, …, record2_valuesn),
…
(recordn_value1, recordn_value2, …, recordn_valuesn);
#在MySQL中，update命令可以同时更新多个表中数据，语法如下：
UPDATE t1,t2,…,tn set t1.field1=expr1,tn.fieldn=exprn [WHERE CONDITION];
# 查询
SELECT * FROM tablename [WHERE CONDITION] [ORDER BY field1[DESC|ASC]，field2 [DESC|ASC],…,fieldn [DESC|ASC]]
# 聚合查询
SELECT [field1,field2,…,fieldn] fun_name 
FROM tablename
[WHERE where_contition][GROUP BY field1,field2,…,fieldn
[WITH ROLLUP]]
[HAVING where_contition]

#fun_name 表示要做的聚合操作，也就是聚合函数，常用的有 sum（求和）、count(*) （记录数）、max（最大值）、min（最小值）。
#GROUP BY关键字表示要进行分类聚合的字段，比如要按照部门分类统计员工数量，部门就应该#写在 group by后面。
#WITH ROLLUP是可选语法，表明是否对分类聚合后的结果进行再汇总。
#HAVING关键字表示对分类后的结果再进行条件的过滤
```
:::warning
having和where的区别在于，having是对聚合后的结果进行条件的过滤，而where是在聚合前就对记录进行过滤，如果逻辑允许，我们尽可能用where先过滤记录，这样因为结果集减小，将对聚合的效率大大提高，最后再根据逻辑看是否用having进行再过滤。
:::


还有比如[左右内全连接](https://www.cnblogs.com/xj-excellent/p/13292291.html)


某些情况下，当进行查询的时候，需要的条件是另外一个select语句的结果，这个时候，就要用到子查询。用于子查询的关键字主要包括 in、not in、=、!=、exists、not exists等。

## DCL语句
将在 [权限与安全](./21security.md) 详细讲解

## 常用网络资源
[MySQL的官方网站](http://dev.mysql.com/downloads)，可以下载到各个版本的MySQL以及相关客户端开发工具等。  
目前[最权威的MySQL数据库及工具的在线手册](http://dev.mysql.com/doc)   
这里可以查看到MySQL已经[发布的bug列表](http://bugs.mysql.com)，或者向MySQL提交bug报告。  
发布各种[关于MySQL的最新消息](http://www.mysql.com/news-and-events/newsletter)。