---
title: Redis系列六 过期策略
date: 2020-11-20
sidebar: auto
categories:
 - redis
tags:
- redis
prev: ./transaction
next: ./seniordata
---

## redis中的数据特征
Redis是一种内存级别的数据库，所有数据均放在内存中，内存中的数据可以通过TTL指令获取其状态
- XX:具有时效性的数据，表示剩余的时间
- -1：永久有效的数据
- -2：已经过期的数据或被删除的数据

```shell
> ttl name #不存在的数据
-2
> set name 12
OK
> expire name 30
1
> ttl name # 剩余过期时间
24
> set age 12
OK
> ttl age #永久有效的数据
-1
> ttl name #过期
-2
> get name #过期后获取不到
null
```

可以看到在设置过期后，超过时间后，将获取不到该值。但是，在高并发下，每秒可能有几万或几十万的指令等待处理，这时候cpu在处理前段的指令会突然增加使用率，然后达到峰值后将会按照指令队列一个个处理，CPU的使用率突然下降。那么这时候，redis就会协调当前任务，将过期删除键值的任务延后。

**那么延后的删除时怎么删除的呢？**

## 删除策略
### 定时删除
创建一个定时器，当key设置有过期时间，且过期时间到达时，由定时器任务立即执行对键的删除操作  

**优点**  
 节约内存，到时就删除，快速释放掉不必要的内存占用  
**缺点**  
 CPU压力很大，无论CPU此时负载量多高，均占用CPU，会影响redis服务器响应时间和指令吞吐量
### 惰性删除
数据到达过期时间，不做处理，等下次访问该数据时删除。
- 如果未过期，返回数据
- 发现已过期，删除该数据，返回不存在

**优点**  
 节约CPU性能，在发现必须要删除的时候才删除  
**缺点**  
 内存压力大，如果某个数据过期且长时间未调用那么就会出现长期占用内存的情况

 ### 定期删除
 Redis启动服务器初始化的时候，读取配置server.hz的值，默认为10

 ```shell
 # Server
redis_version:6.0.6
redis_git_sha1:00000000
redis_git_dirty:0
redis_build_id:19d4277f1e8a2fed
redis_mode:standalone
os:Linux 3.10.0-1127.18.2.el7.x86_64 x86_64
arch_bits:64
multiplexing_api:epoll
atomicvar_api:atomic-builtin
gcc_version:8.3.0
process_id:1
run_id:34926c9a8f577eb22ba21c50dfea8f30932e6de4
tcp_port:6379
uptime_in_seconds:77176
uptime_in_days:0
hz:10 ###
configured_hz:10
lru_clock:13088228
executable:/data/redis-server
config_file:/etc/redis/redis.conf
 ```

这个值是控制每秒钟调用server.hz次<font color=#0099ff face="黑体">serverCron()</font>函数，serverCron()函数顾名思义就可以知道是一个定时任务，而个函数调用的<font color=#0099ff face="黑体">databaseCron()</font>函数是轮询每个库（redis分为16个库），轮询操作是用来调用<font color=#0099ff face="黑体">activeExpireCycle()</font>这个函数。

而activeExpireCycle对每个库中的对应的expires数组进行测，每次执行250ms/server.hz这时间

对某个expires[\*]进行检测时，随机挑选W个key检测，检测除key
【W=ACTIVE_EXPIRE_CYCLE_LOOKUPS_PER_LOOP属性值（配置文件设定）】  
- 如果超时，那么就删除key。  
- 如果一轮中删除的key的数量大于W\*25%，那么就循环该过程，再次检测当前库。   
- 如果一轮中删除key的数量小于W\*25%，那么检查下一个库的expires。

简而言之，databaseCron（）是对多个库的检查，activeExpireCycle()是对单个库做检查，serverCron()是每秒执行多少次

如果在activeExpireCycle()调用时，检测时间到期了，那么会将当前的库名存储到参数current_db记录，下次从current_db继续轮询执行

定期删除是周期性轮询redis库中时效性数据，采用随机抽取的策略，利用过期数据占比的方式控制删除频度

**特点**
- CPU性能占用设置有峰值，检测频度可自定义设置
- 内存压力不是很大，长期占用内存的冷数据会被持续清理

## 逐出算法（淘汰算法）
当新数据进入redis时，如果内存不住怎么办？

Redis存储数据时，在执行每一个命令前，会调用freeMemoryIfNeed()检测内存是否充足。如果内存不满足新加入数据的最低存储要求，redis要临时删除一些数据为当前指令清理存储空间。清理数据的策略成为逐出算法。

:::warning 注意
逐出数据的过程不是100%能够清理出足够的可使用的内存空间，如果不成功则反复执行。当对所有数据尝试完毕后，如果不能达到内存清理的要求，将出现错误信息
`(error) OOM command not allowed when used memory>maxmemory`
:::

- 最大可使用内存  
`maxmemory`  
是在配置和文件中设置的，表示占用物理内存的比例，默认为0，表示不限制。生产环境中根据需求设定，通常设置在50%以上。

- 每次选取待删除数据的个数  
`maxmemory-sample`  
选取数据时不会全库扫描，导致严重的性能消耗，降低读写性能。因此采用随机获取数据的方式作为待检测删除数据

- 删除策略  
`maxmemory-policy`  
达到最大内存后，对被挑选出来的数据进行删除的策略

### 影响数据逐出的配置
- **检测容易失效的数据（可能会过期的数据集，也就是在server.db[i].expires）**
1. volatile-lru(Least Recently Used):挑选最长时间没有使用的数据淘汰
2. volatile-lfu(Least Frequently Used):挑选最近使用次数最少的数据淘汰
3. volatile-ttl：挑选将要过期的数据淘汰
4. volatile-random：随机选择数据淘汰

- **检测全库数据（所有数据集server.db[i].dict）**
1. allkeys-lru: 挑选最长时间没有使用的数据淘汰
2. allkeys-lfu：挑选最近使用次数最少的数据淘汰
3. allkeys-random: 随机选择数据淘汰

- **放弃数据驱逐**
1. no-enviction:禁止驱逐数据（redis4.0中默认策略），会引发OOM

### 数据逐出策略配置依据
使用INFO指令输出监控信息，查询缓存hit和miss次数，根据业务需求调优redis配置

```shell
##persistence
active_defrag_hits:0
active_defrag_misses:0
active_defrag_key_hits:0
active_defrag_key_misses:0
```