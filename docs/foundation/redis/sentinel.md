---
title: Redis系列九 哨兵模式
date: 2020-11-30
sidebar: auto
categories:
 - redis
tags:
- redis
prev: ./replication
next: ./cluster
---

## 简介
在之前的主从复中，提到了当master宕机下线后，会找一个slave作为master继续执行，但是，由谁来确认master宕机呢？怎么找slave，找哪一个slave作为master呢？修改配置后，发现原来的master恢复了怎么办？

哨兵（sentinel）是一个分布式系统，用于对主从结构中每个节点进行监控，当出现故障时通过投票机制选择新的master并将所有的slave连接到master
主要有三个功能：
- 监控： 不断检测maste和salve是否正常运作，master存货检测、master与slave运行情况检测等
- 通知： 当被监控的节点出现问题时，向其他哨兵发送通知
- 自动故障转移： 断开master与slave连接，选取一个slave作为master，将其他 slave连接到新的master，并告知客户端新的服务器地址

:::warning 注意
哨兵也是服务，也要独立于redis启动，同时通常将哨兵数量设置为单数，以免发送选举平票的情况
:::

## 创建setinel
首先配置文件如下：
```shell
port 26379
sentinel monitor mymaster 192.168.83.16 6379 2 #mymaster为自定义的sentinel名，后面的ip和端口为master，2表示有多少个哨兵认定挂了就确定为挂了
#sentinel auth-pass mymaster admin #sentinel 密码
sentinel down-after-milliseconds mymaster 15000 # 设置多少毫秒内无响应认定节点挂了
sentinel parallel-syncs mymaster 1 #使用多少个cpu去执行重新选举后执行的数据同步工作
sentinel failover-timeout mymaster 80000 #数据同步在多少秒内未完成表示失败
bind 0.0.0.0
```
建立sentinel.yml的docker-compose 文件
```yml
version: '3'
services:
  s26379:
    image: redis:latest
    restart: always
    container_name: s26379
    ports:
      - 26379:26379
    volumes:
      - /usr/local/setinel/sentinel.conf:/etc/redis/sentinel.conf
    command:
      redis-sentinel /etc/redis/sentinel.conf
  s26380:
    image: redis:latest
    restart: always
    container_name: s26380
    ports:
      - 26380:26379
    volumes:
      - /usr/local/sentinel/sentinel.conf:/etc/redis/sentinel.conf
    command:
      redis-sentinel /etc/redis/sentinel.conf
  s26381:
    image: redis:latest
    restart: always
    container_name: s26381
    ports:
     - 26381:26379
    volumes:
      - /usr/local/sentinel/setinel.conf:/etc/redis/sentinel.conf
    command:
      redis-sentinel /etc/redis/sentinel.conf
```

哨兵启动完毕后，将会重写sentinel.conf文件，配置sentinel集群的信息等

## 工作原理

