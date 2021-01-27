---
title: Redis系列四 持久化
date: 2020-11-16
sidebar: auto
categories:
 - redis
tags:
- redis
prev: ./lettuce
next: ./transaction
---
## 简介
利用永久存储介质将数据保存，在特定时间将保存的数据进行恢复的工作机制称为持久化。
- 将当前数据状态进行保存、快照形式，存储数据结果。存储格式简单(RDB)
- 将数据操作过程进行保存、日志形式，存储操作过程。存储格式复杂(AOF)

## 一、RDB
`save`执行手动执行一次保存操作

### 1.1 save指令相关配置
- dbfilename dump.rdb
设置本地数据库文件名，默认为dump.rdb,在多台集群的情况下，通常设置为dump-**端口号**.rdb
- dir
设置存储rdb文件的路径，通常设置成存储空间较大的目录中，目录名称为rdb
- rdbcompression yes
设置存储至本地数据库时是否压缩数据，默认为yes，采用LZF压缩，通常设置成开启状态，如果设置为关闭，那么会使存储的文件变得巨大
- rdbchecksum yes
设置是否进行rdb文件格式校验，该校验过程在读和写文件的操作均可以进行，通常设置为开启，如果设置为关闭，虽然可以节省10%左右的读写时间，但是存储会有数据损坏的风险
- stop-writes-on-bgsave-error yes
设置后台存储过程中如果出现错误现象，是否停止保存操作。通常设置为开启，如果不开启可能会损坏rdb文件
- save second changes
设置满足限定时间范围内key的变化数量达到指定数量即进行持久化（ second监控时间范围 changes监控key的变化量） 
```shell
save 900 100
```

save配置要根据实际业务需求进行配置，频度过高或者过低都会出现性能问题，结果可能使灾难性的，对于second和changes设置通常具有互补对应关系，尽量不要设置成包含性关系，save配置启动后执行的是bgsave操作
### 1.2 save指令工作原理
由于redis的任务执行序列为单线程任务执行序列，所以save指令会阻塞当前redis服务器，直到rdb的操作都完成。这个过程可能会造成长时间的阻塞，在线上环境不建议使用。通常使用`bgsave`来替代save操作
bgsave操作会调用fork函数生成一个子进程，创建rdb文件，然后返回客户端。

bgsave命令是针对save问题做的优化，redis内部所有有关save的操作都采用save方式，通常不用save指令而使用bgsave替代


---
**优点**
- rdb是一个紧凑压缩的二进制文件，存储效率高，
- rdb内部存储的是redis在某个时间点的数据快照，非常适合用于数据备份，全量复制等操作，
- 恢复数据比aof速度要快很多


**缺点**
- 无法做到实时初始化，具有较大的丢失数据的可能性，
- bgsave指令每次操作时都会创建子进程，会牺牲一些性能，
- redis的众多版本中未进行rdb文件格式的版本统一，有可能出现各版本服务之间数据格式无法兼容的现象



## 二、AOF
AOF(append only file)持久化：以独立日志的方式记录每次写命令，重启时再重新执行aof文件中的命令达到恢复数据的目的。AOF的主要作用是解决了数据持久化的实时性问题，目前是redis持久化的主流方式

### 2.1 AOF写数据的三种策略
- always（每次）  
每次写操作均同步到aof文件中，数据零误差，性能较低 不建议使用
- everysec(每秒)  
每秒将缓冲区中的指令同步到aof文件中，数据准确度高，性能高，也是默认的配置，但是再系统宕机的情况下会丢失1秒内的数据
- no（系统控制）  
由操作系统控制每次同步到aof文件的周期，整体过程不可控

### 2.2 AOF配置
- appendonly yes|no
是否开启aof持久化功能，默认不开启
- appendfsync always|everysec|no
aof写数据策略
- auto-aof-rewrite-min-size size 自动触发重写的最小文件大小  
对比参数为aof_current_size （运行指令info 中persistence获取具体信息）
当aof_current_size >auto-aof-rewrite-min-size 触发
- auto-aof-rewrite-percentage  percent 自动触发重写百分比大小  
对比参数aof_base_size,aof_current_size （运行指令info 中persistence获取具体信息）
当(aof_current_size - aof_base_size)/aof_base_size>=auto-aof-rewrite-percentage触发重写


### 2.3 AOF重写
随着命令不断写入AOF文件，文件也会变得越来越大，为了解决这个问题，redis引入了aof重写机制压缩文件体积，aof文件重写是将redis进程内的数据转化为写命令同步到新aof文件的过程，简单来说就是将对同一个数据的若干条命令执行结果转化为最终结果数据对应的指令进行记录。使用指令 `bgrewriteaof`，在使用这个指令时，redis响应和bgsave指令类似，通过fork函数生成子进程，重写aof缓冲区，然后生成重写后的aof与旧的aof文件合并，注意：在always写策略时，是没有aof缓冲区的，所以always策略是非重写的

AOF重写能减低磁盘占用量，提高磁盘利用率。提高持久化效率，降低持久化写时间，提高IO性能。降低数据恢复用时，提高数据恢复效率。


**重写规则**

- 进程内已超时的数据将不再写入文件
- 忽略无效指令，重写时使用进程内数据直接生成，这样新的AOF文件只保留最终数据的写入命令
- 对同一数据的多条写命令合并为一条命令 如：`lpush list a、lpush list b、lpush list c`转化为：`lpush list a b c`。为防止数据量过大造成客户端缓冲区溢出，对list、set、hash、zset等类型重写，每条指令最多写入64个元素

