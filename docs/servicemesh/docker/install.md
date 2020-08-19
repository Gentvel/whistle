---
title: 【Docker】 Docker安装
date: 2020-08-19
sidebar: auto
prev: false
next: false
---

## Centos 7 安装docker
1. Docker 要求 CentOS 系统的内核版本高于 3.10 ，查看本页面的前提条件来验证你的CentOS 版本是否支持 Docker 。  

通过 uname -r 命令查看你当前的内核版本
```shell
uname -r
```
2. 使用 root 权限登录 Centos。确保 yum 包更新到最新。
```shell
yum update
```
3. 卸载旧版本(如果安装过旧版本的话)
```shell
yum remove docker  docker-common docker-selinux docker-engine
```
4. 安装需要的软件包， yum-util 提供yum-config-manager功能，另外两个是devicemapper驱动依赖的
```shell
yum install -y yum-utils device-mapper-persistent-data lvm2
```
5. 设置yum源
```shell
yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
```

6. 可以查看所有仓库中所有docker版本，并选择特定版本安装
```shell
yum list docker-ce --showduplicates | sort -r
```

7. 安装docker
```shell
 yum install docker-ce  #由于repo中默认只开启stable仓库，故这里安装的是最新稳定版17.12.0
 yum install <FQPN>  # 例如：sudo yum install docker-ce-17.12.0.ce
 ```

8. 启动并加入开机启动
```shell
systemctl start docker
systemctl enable docker
```

9. 配置国内源
```shell
mkdir -p /etc/docker
vi daemon.json
```
10. deamon.json 配置
```shell
{
  "registry-mirrors": ["https://registry.docker-cn.com"]
}
```
或者使用[阿里源](https://cr.console.aliyun.com/#/accelerator)  

11. 重启deamon
```shell
systemctl daemon-reload
systemctl restart docker
```

12. 验证安装是否成功(有client和service两部分表示docker安装启动都成功了)
```shell
docker version #查看docker版本信息
docker info #查看详细信息
```

## 安装Docker-Compose
#### 简单介绍
Docker Compose 是一个用来定义和运行复杂应用的 Docker 工具。
使用 Docker Compose 不再需要使用 shell 脚本来启动容器。(通过 docker-compose.yml 配置)
1. 安装
```shell
curl -L https://get.daocloud.io/docker/compose/releases/download/1.22.0/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
```
2. 卸载
```shell
rm /usr/local/bin/docker-compose
```
