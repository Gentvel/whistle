---
title: Redis系列七 高级数据类型
date: 2020-11-23
sidebar: auto
categories:
 - redis
tags:
- redis
prev: ./expire
next: ./replication
---

高级数据类型是为了解决特定的问题而产生的。
## Bitmaps
在开发当中可能会遇到统计一些年度最佳数据，月度最佳数据，周最佳数据等，一般情况下最先能想到的就是使用数据库查询。这种非黑即白的数据，可以使用计算机中最小存储单位bit作为存储方式，一般计算机一个bit有8个位，可以根据位判断某一个数据是否为真或者假。
比如：统计全班人的性别  0100 0010， 0000 0000 ....
bitmaps表示就如上，1表示男，其它表示女，第一位表示学号为1的学员，以此类推。
但是这样又会产生一个问题，那就是设值和取值不方便。但对于我们使用redis来说，取值只要知道数据中的为止即可，设值需要知道位置和修改的结果。

### 基础操作
- 获取指定key对应偏移量上的bit值  
`getbit key offset`

- 设置指定key对应偏移量上的bit值，value只能是0或1  
`setbit key offset 0|1`


```shell
> setbit bit 0 1 #设置值
0
> getbit bit 0 #获取值
1
> getbit bit 10 #获取不存在的值会返回0
0
> getbit bit 100000 #获取不存在的值会返回0
0
> setbit bit 10000000 1 #当设置位数比较大的时候，需要在前面补0，这样的操作比较耗时
0
```

### 拓展操作
现在有一个业务场景是视频网站需求
- 统计每天某视频是否被点播  
假如某视频的id为6，位图的值为01001000，假如该视频被播放，那么就将第6位的值改成1，01001100

- 统计每天有多少视频被点播  
统计每天有多少视频被播放只需要查询bitmaps里面有多少位是1的数据即可

- 统计每周、月、年有多少视频被点播  
统计每周每天对前天的数据进行统计按或操作，统计1的个数

01001110   
10011101 =   
11011111  

然后再对后一天的数据进行或操作，将最后的结果统计1的个数

- 对指定key按位进行交、或、非、异或操作，并将结果保存到destkey中  
`bitop op destkey key1 key2 ...`  

op类型有：and交，or或，not非，xor异或

- 统计指定key中1的数量  
`bitcount key [start end]`

```shell
> setbit 20201203 0 1
0
> setbit 20201203 5 1
0
> setbit 20201203 8 1
0
> setbit 20201204 1 1
0
> setbit 20201204 2 1
0
> setbit 20201204 5 1
0
> setbit 20201204 8 1
0
> bitcount 20201203 #当不表明结束或开始时默认全部位
3
> bitcount 20201204
4
> bitcount 20201204 0 10
4
> bitop or 03-04 20201203 20201204
2
> bitcount 03-04
5
```

## HyperLogLog
用于做基数统计
比如现在有一个集合{1，1，1，1，3} 那么基数集{1，3} 基数就是2  
深入LogLog算法可查看[loglog算法](https://blog.csdn.net/firenet1/article/details/77247649)

### 基本操作
- 添加数据  
`pfadd key element [element]...`
- 统计数据  
`pfcount key [key]...`
- 合并数据  
`pfmerge destkey sourcekey [sourcekey]...`

```shell
> pfadd hll 001
1
> pfadd hll 001
0
> pfadd hll 001
0
> pfadd hll 001
0
> pfadd hll 001
0
> pfadd hll 001
0
> pfadd hll 002
1
> pfadd hll 002
0
> pfcount hll
2
```
主要用于独立信息统计，比如某天访问量，使用用户id添加至hyperloglog，最后用pfcount统计，如果统计某周某月，使用pfmerge合并然后pfcount即可。

:::warning 注意
HyperLogLog只是用于基数统计，不保存数据，只记录数量  
核心是基数估算算法，最终数值存在一定误差  
误差范围：基数估计结果是一个带有0.81%标准错误的近似值  
每个hyperloglogkey占用了12k的内存用于标记基数，消耗空间极小  
pfadd命令不是一次性分配12K内存使用，会随着基数增加内存组件增大  
pfmerge命令合并后占用的存储空间为12K,无论合并之前数据量多少
:::

## GEO
地理位置操作

### 基本操作
- 添加坐标点  
`geoadd key longitude latitude member [longitude latitude member...]`  
- 获取坐标点  
`geopos key member[member ...]`
- 计算坐标点距离  
`geodist key member1 member2 [unit]`

```shell
> geoadd geos 1 1 a
1
> geoadd geos 2 2 b
1
> geopos geos a
0.99999994039535522
0.99999945914297683
> geodist geos a b
157270.0561
> geodist geos a b m
157270.0561
> geodist geos a b km
157.2701
```

- 根据坐标求范围内的数据  
`georadius key longitude latitude radius m|km|ft|mi [withcoord] [withdist] [withhash] [count count] [desc|asc]`
- 根据点求范围内数据  
`georadiusbymember key member radius m|km|ft|mi [withcoord] [withdist] [withhash] [count count] [desc|asc]`
- 获取指定点对应的坐标hash值   
`geohash key member[member...]`

```shell
> geoadd geos 1 2 c
1
> geoadd geos 2 1 d
1
> georadiusbymember geos c 180 km #查找距离c点180km以内的点
a
d
c
b
> georadiusbymember geos c 120 km
c
b
a
> georadiusbymember geos c 100 km
c
> georadius geos 1.5 1.5 100 km #查找距离点（1.5， 1.5）100km以内的点
c
b
a
d
> geohash geos a
s00twy01mt0
```

通常，使用georadiusbymember用来做定点之间的距离计算，使用georadius用来做动点到顶点的距离计算，且不用添加到geo集合内