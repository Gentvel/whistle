---
title: Redis系列二 通用指令
date: 2020-10-12
sidebar: auto
prev: ./datatype
next: ./lettuce
---

## 一、key通用操作
key是一个字符串，通过key获取redis中保存的数据
```shell
> del sset #删除指定key
1
> exists sset #查询key是否存在
0
> type news #获取key类型
set
```

命令|描述
-|-
DEL key|该命令用于在 key 存在时删除 key。
DUMP key|序列化给定 key ，并返回被序列化的值。
EXISTS key|检查给定 key 是否存在。
EXPIRE key seconds|为给定 key 设置过期时间，以秒计。
EXPIREAT key timestamp|EXPIREAT 的作用和 EXPIRE 类似，都用于为 key 设置过期时间。 不同在于 EXPIREAT 命令接受的时间参数是 UNIX 时间戳(unix timestamp)。
PEXPIRE key milliseconds|设置 key 的过期时间以毫秒计。
PEXPIREAT key milliseconds-timestamp|设置 key 过期时间的时间戳(unix timestamp) 以毫秒计
KEYS pattern|查找所有符合给定模式( pattern)的 key 。
MOVE key db|将当前数据库的 key 移动到给定的数据库 db 当中。
PERSIST key|移除 key 的过期时间，key 将持久保持。
PTTL key|以毫秒为单位返回 key 的剩余的过期时间。
TTL key|以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
RANDOMKEY|从当前数据库中随机返回一个 key 。
RENAME key newkey|修改 key 的名称
RENAMENX key newkey|仅当 newkey 不存在时，将 key 改名为 newkey 。
SCAN cursor [MATCH pattern] [COUNT count]|迭代数据库中的数据库键。
TYPE key|返回 key 所储存的值的类型。


## 二、数据库操作
命令|描述
-|-
BGREWRITEAOF|异步执行一个 AOF（AppendOnly File） 文件重写操作
BGSAVE|在后台异步保存当前数据库的数据到磁盘
CLIENT KILL [ip:port] [ID client-id]|关闭客户端连接
CLIENT LIST|获取连接到服务器的客户端连接列表
CLIENT GETNAME|获取连接的名称
CLIENT PAUSE timeout|在指定时间内终止运行来自客户端的命令
CLIENT SETNAME connection-name|设置当前连接的名称
CLUSTER SLOTS|获取集群节点的映射数组
COMMAND|获取 Redis 命令详情数组
COMMAND COUNT|获取 Redis 命令总数
COMMAND GETKEYS|获取给定命令的所有键
TIME|返回当前服务器时间
COMMAND INFO command-name [command-name ...]|获取指定 Redis 命令描述的数组
CONFIG GET parameter|获取指定配置参数的值
CONFIG REWRITE|对启动 Redis 服务器时所指定的 redis.conf 配置文件进行改写
CONFIG SET parameter value|修改 redis 配置参数，无需重启
CONFIG RESETSTAT|重置 INFO 命令中的某些统计数据
DBSIZE|返回当前数据库的 key 的数量
DEBUG OBJECT key|获取 key 的调试信息
DEBUG SEGFAULT|让 Redis 服务崩溃
FLUSHALL|删除所有数据库的所有key
FLUSHDB|删除当前数据库的所有key
INFO [section]|获取 Redis 服务器的各种信息和统计数值
LASTSAVE|返回最近一次 Redis 成功将数据保存到磁盘上的时间，以 UNIX 时间戳格式表示
MONITOR|实时打印出 Redis 服务器接收到的命令，调试用
ROLE|返回主从实例所属的角色
SAVE|同步保存数据到硬盘
SHUTDOWN [NOSAVE] [SAVE]|异步保存数据到硬盘，并关闭服务器
SLAVEOF host port|将当前服务器转变为指定服务器的从属服务器(slave server)
SLOWLOG subcommand [argument]|管理 redis 的慢日志
SYNC|用于复制功能(replication)的内部命令

## 三、连接命令
命令|描述
-|-
AUTH password|验证密码是否正确
CONFIG SET requirepass < password>| 配置密码
CONFIG GET requirepass | 查看密码
ECHO message|打印字符串
PING|查看服务是否运行
QUIT|关闭当前连接
SELECT index|切换到指定的数据库

## 四、脚本命令
命令|描述
-|-
EVAL script numkeys key [key ...] arg [arg ...] |执行 Lua 脚本。
EVALSHA sha1 numkeys key [key ...] arg [arg ...] |执行 Lua 脚本。
SCRIPT EXISTS script [script ...] |查看指定的脚本是否已经被保存在缓存当中。
SCRIPT FLUSH|从脚本缓存中移除所有脚本。
SCRIPT KILL|杀死当前正在运行的 Lua 脚本。
SCRIPT LOAD script|将脚本 script 添加到脚本缓存中，但并不立即执行这个脚本。

## 五、配置

### 基础配置

- 设置服务器以守护进程的方式运行  
`daemonize yes|no`  
daemonize:yes ：在该模式下，redis会在后台运行，并将进程pid号写入至redis.conf选项pidfile设置的文件中，此时redis将一直运行，除非手动kill该进程。  
daemonize:no ：当daemonize选项设置成no时，当前界面将进入redis的命令行界面，exit强制退出或者关闭连接工具(putty,xshell等)都会导致redis进程退出。

- 绑定主机地址  
`bind 127.0.0.1`   
绑定了主机地址后，客户端只能通过该ip访问，如果不绑定可以通过比如localhost访问，

- 设置服务器端口  
`port 6379`  

- 设置数据库数量  
`databases 16 `

- 设置数据库密码  
`requirepass <password>`

protected-mode yes时，不设置密码只能进行本地连接，设置密码以后可以外网连接。
protected-mode no时，不设置密码也可以外网连接。

### 日志配置

- 设置服务器以指定的日志记录级别  
`loglevel debug|verbose|notice|warning`  
日志级别开发期设置为verbose即可（默认），生产环境中配置为notice，简化日志输出量，降低写日志的IO频度

- 记录日志文件名  
`logfile 端口号.log` 

### 客户端配置

- 设置统一时间最大客户端连接数，默认无限制。当客户端达到上限，redis会关闭新的连接  
`maxclients 0`

- 客户端限制等待最大时长，达到最大值后关闭连接。 关闭该功能设置为0即可  
`timeout 300`

### 多服务器快捷配置
- 导入并加载指定配置文件信息，用于快速创建redis公共配置较多的redis实例配置文件，便于维护  
`include /path/server-端口号.conf`
