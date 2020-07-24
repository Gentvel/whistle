---
title: 【Linux】常用基本命令
date: 2020-07-22
categories:
 - 基础
tags:
- linux
---
## 一、基本命令
*基于centos系统版本命令*
### 1.1 防火墙相关
- #### 进程与状态相关
```
systemctl start firewalld.service            #启动防火墙  
systemctl stop firewalld.service             #停止防火墙  
firewall-cmd --state                         #查看防火墙状态  
firewall-cmd --state                         #查看防火墙状态  
firewall-cmd --reload                        #重载防火墙规则  
firewall-cmd --list-ports                    #查看所有打开的端口  
firewall-cmd --list-services                 #查看所有允许的服务  
firewall-cmd --get-services                  #获取所有支持的服务 
```
- #### 区域相关
```
firewall-cmd --list-all-zones                    #查看所有区域信息  
firewall-cmd --get-active-zones                  #查看活动区域信息  
firewall-cmd --set-default-zone=public           #设置public为默认区域  
firewall-cmd --get-default-zone                  #查看默认区域信息  
firewall-cmd --zone=public --add-interface=eth0  #将接口eth0加入区域public
```
- #### 端口控制

```
firewall-cmd --add-port=80/tcp --permanent               #永久添加80端口例外(全局)
firewall-cmd --remove-port=80/tcp --permanent            #永久删除80端口例外(全局)
firewall-cmd --add-port=65001-65010/tcp --permanent      #永久增加65001-65010例外(全局)  

firewall-cmd  --zone=public --add-port=80/tcp --permanent            #永久添加80端口例外(区域public)
firewall-cmd  --zone=public --remove-port=80/tcp --permanent         #永久删除80端口例外(区域public)
firewall-cmd  --zone=public --add-port=65001-65010/tcp --permanent   #永久增加65001-65010例外(区域public) 
```
- #### 指定ip段可以访问与不能访问
```
#指定ip段可以访问
firewall-cmd --permanent --add-rich-rule="rule family="ipv4" source address="192.168.2.0/24" port protocol="tcp" port="5432" accept"
#指定ip不可以访问
firewall-cmd --permanent --add-rich-rule="rule family="ipv4" source address="192.168.2.0/24" port protocol="tcp" port="5432" reject"
```

**注：如果某个接口不属于任何Zone，那么这个接口的所有数据包使用默认的Zone的规则。**

命令含义：
  - --zone #作用域
  - --add-port=80/tcp #添加端口，格式为：端口/通讯协议
  - --permanent #永久生效，没有此参数重启后失效

### 1.2 Systemctl命令
```
systemctl start firewalld.service               #启动服务
systemctl stop firewalld.service                #关闭服务
systemctl reloadt firewalld.service             #重载配置
systemctl restart firewalld.service             #重启服务
systemctl status firewalld.service              #显示服务的状态
systemctl enable firewalld.service              #在开机时启用服务
systemctl disable firewalld.service             #在开机时禁用服务
systemctl is-enabled firewalld.service          #查看服务是否开机启动
systemctl list-unit-files|grep enabled          #查看已启动的服务列表
systemctl --failed                              #查看启动失败的服务列表

```
### 1.3 关闭CentOS7自带Firewall启用iptables
```
yum install iptables-services           #安装iptables  
systemctl stop firewalld.service        #停止firewalld  
systemctl mask firewalld.service        #禁止自动和手动启动firewalld  
systemctl start iptables.service        #启动iptables
systemctl start ip6tables.service       #启动ip6tables  
systemctl enable iptables.service       #设置iptables自启动  
systemctl enable ip6tables.service      #设置ip6tables自启动  
```
*注：静态防火墙规则配置文件是`/etc/sysconfig/iptables`以及`/etc/sysconfig/ip6tables`*

### 1.4 Linux释放内存

```
#释放网页缓存(To free pagecache):
sync; echo 1 > /proc/sys/vm/drop_caches

#释放目录项和索引(To free dentries and inodes):
sync; echo 2 > /proc/sys/vm/drop_caches

#释放网页缓存，目录项和索引（To free pagecache, dentries and inodes):
sync; echo 3 > /proc/sys/vm/drop_caches
```

## 二、插件命令
### 2.1 YUM 清理缓存
清理yum缓存使用yum clean 命令，yum clean 的参数有headers, packages, metadata, dbcache, plugins, expire-cache, rpmdb, all

```
yum clean headers  #清理/var/cache/yum的headers
yum clean packages #清理/var/cache/yum下的软件包
yum clean metadata
```