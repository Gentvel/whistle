---
title: Redis系列三 Lettuce（生菜）客户端
date: 2020-11-15
sidebar: auto
categories:
 - redis
tags:
- redis
prev: ./command
next: ./rdbaof
---

## Redis客户端
redis客户端有很多种，
- Jedis：是Redis的Java实现客户端，提供了比较全面的Redis命令的支持。但其为阻塞式io，性能方面稍显不足
- Lettuce：高级Redis客户端，用于线程安全同步，异步和响应使用，支持集群，Sentinel，管道和编码器。spring在较新的版本后默认的redis客户端