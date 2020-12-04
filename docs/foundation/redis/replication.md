---
title: Redis系列八 主从复制
date: 2020-11-01
sidebar: auto
prev: ./seniordata
next: false
---

## 简介
单机redis的风险与问题
1. 机器故障，如硬盘故障或系统崩溃，造成数据丢失，很有可能对业务造成灾难性的打击
2. 容量瓶颈，内存不足的情况是很常发生的

:::tip
为了避免单点redis服务器故障，准备多台服务器，互相连通。将数据复制多个副本保存到不同的服务器上，连接到一起，并保证数据是同步的。即使有其中一台服务器宕机，其他服务器依然可以提供服务，实现redis高可用，同时实现数据冗余备份。
:::

<center>

![Redis Replication](./img/replication.png)

</center>

以上就是主从复制的连接方案，有master负责收据数据，然后同步到slave，由slave提供读取数据的服务，并且一个slave只能对应一个master

但是以上方式还会有问题，一个slave宕机，对整体集群的影响不大，但是如果master宕机了，那集群就不能对外提供写数据的服务，这时候集群会从slave中推选除一个master，用来收集数据。这样就能提供高可用的服务了

## 作用
- 读写分离：master写，slave读，提高服务器的读写能力
- 负载均衡：基于主从结构，配合读写分离，由slave分担master负载，并根据需求的变化，改变slave的数量，通过多个从节点分担数据读取负载，提高redis服务并发量与数据吞吐量
- 故障恢复：当master出现问题，由slave提供服务，实现快速的故障恢复
- 数据冗余：实现数据热备份，是持久化之外的另一种数据冗余方式


## 工作流程

主从复制过程可以分为3个阶段：  
### 1.建立连接阶段（由slave连接master）
建立slave到master的连接，使master能够识别slave，并保存slave端口号

<center>

![Redis Replication](./img/conn.png)

</center>


以上就是连接的步骤。

主从连接的操作由如下几种：

1. 客户端发送命令  
`slaveof <masterip> <masterport>`
如果master配置密码的话则需要使用命令 
`config set masterauth password`
2. 服务器启动参数  
`redis-server --slaveof <masterport>`

3. 服务器配置  
`slaveof <masterip> <masterport>`  
配置文件配置  
`masterauth password`

当然，还有很多方式，比如docker，docker-compose和k8s等
由于测试的时候使用两个虚拟机docker测试，使用docker-compose 启动，修改command命令即可

- 主从断开连接  
`slaveof no one`


slave系统信息
```shell
# Replication
role:slave
master_host:192.168.83.16
master_port:6379
master_link_status:up
master_last_io_seconds_ago:9
master_sync_in_progress:0
slave_repl_offset:70
slave_priority:100
slave_read_only:1
connected_slaves:0
master_replid:55e97ffcc89f1610164d2d7fac0d96a08a30c635
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:70
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:70
```

master系统信息
```shell
# Replication
role:master
connected_slaves:1
slave0:ip=192.168.83.15,port=6379,state=online,offset=112,lag=0
master_replid:55e97ffcc89f1610164d2d7fac0d96a08a30c635
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:112
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:112
```
当slave中的信息role为slave且master_link_status:up说明通信成功


### 2.数据同步阶段
slave通过指令请求数据，master接收指令后执行`bgsave`，但是此时会出现一个问题，就是接收到新的set指令，这样rdb的数据是没有当前新数据的，为了解决这个问题，master在第一个slave连接的时候会在执行`bgsave`指令同时创建一个命令缓冲区。然后将生成的rdb文件通过连接阶段的socket发送到slave。slave接收到RDB文件后，清空当前数据并执行RDB文件的恢复过程。这个过程叫做全量复制。

全量复制完成后，slave发送命令告知masterRDB恢复已经完成，master接收到后发送复制缓冲区的aof文件，并记录当前发送的aof指令的数量，保证数据完整性。slave接收到文件后执行bgrewriteaof恢复数据，这个阶段叫做部分复制（增量复制）

在开始部分复制前，

<center>

![Redis Sync](./img/sync.png)

</center>

```shell
1:M 02 Dec 2020 21:39:48.058 * Full resync requested by replica 192.168.83.15:6379 #slave请求同步数据
1:M 02 Dec 2020 21:39:48.058 * Replication backlog created, my new replication IDs are '55e97ffcc89f1610164d2d7fac0d96a08a30c635' and '0000000000000000000000000000000000000000'
1:M 02 Dec 2020 21:39:48.058 * Starting BGSAVE for SYNC with target: disk 
1:M 02 Dec 2020 21:39:48.062 * Background saving started by pid 16 #开始bgsave
16:C 02 Dec 2020 21:39:48.083 * DB saved on disk 
16:C 02 Dec 2020 21:39:48.085 * RDB: 4 MB of memory used by copy-on-write
1:M 02 Dec 2020 21:39:48.144 * Background saving terminated with success #bgsave完成
1:M 02 Dec 2020 21:39:48.145 * Synchronization with replica 192.168.83.15:6379 succeeded #同步完成
```

:::warning master注意
1. 如果master数据量巨大，数据同步阶段应避开流量高峰期，避免造成master阻塞，影响业务正常执行
2. 复制缓冲区大小设置不合理，会导致数据溢出。如进行全量复制周期太长，进行部分复制时发现数据已经存在丢失的情况，必须进行第二次全量复制，致使slave陷入死循环状态。
3. master单机内存占用主机内存的比例不应过大，建议使用50%-70%，留下部分内存用于执行bgsave命令和创建复制缓冲区
:::

设置复制缓冲区配置文件为：  
`repl-backlog-size 1mb`


:::warning slave注意
1. 为避免slave进行全量复制、部分复制时服务器响应阻塞或数据不同步，建议关闭此期间的对外服务
2. 如果多个slave同时对master请求数据同步，master发送的RDB文件增多，会对带宽造成巨大冲击，造成master带宽不足，因此数据同步需要根据业务实际需求适量错峰
3. slave过多时，建议调整拓扑结构，由一主多从结构变为树状结构，中间的节点即是master，也是slave。注意使用树状结构时，由于层级深度，导致深度越高的slave与最顶层的master之间的数据同步延迟较大，数据一致性变差，应该谨慎选择
:::

设置slave在复制阶段关闭对外服务：  
`slave-serve-stable-data yes|no`
### 3.命令传播阶段
当master数据库状态被修改后，导致主从服务器数据库状态不一致，此时需要让主从数据同步到一致的状态，同步状态的动作就称为命令传播。

如果命令传播阶段出现了断网现象
情况|方案
-|-
网络闪断闪连|忽略
短时间网络中断|部分复制
长时间网络中断|全量复制

