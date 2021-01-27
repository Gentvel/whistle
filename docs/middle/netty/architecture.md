---
title: 【Netty】 Netty架构衍变
date: 2020-12-18
sidebar: auto
categories:
 - 中间件
tags:
- netty
prev: ./copy
next: ./component
---

## 架构演变

### 原生NIO存在的问题
NIO 的类库和 API 繁杂，使用麻烦：需要熟练掌握 Selector、ServerSocketChannel、SocketChannel、ByteBuffer 等。

需要具备其他的额外技能：要熟悉 Java 多线程编程，因为 NIO 编程涉及到 Reactor 模式，你必须对多线程和网络编程非常熟悉，才能编写出高质量的 NIO 程序。

开发工作量和难度都非常大：例如客户端面临断连重连、网络闪断、半包读写、失败缓存、网络拥塞和异常流的处理等等。

JDK NIO 的 Bug：例如臭名昭著的 Epoll Bug，它会导致 Selector 空轮询，最终导致 CPU 100%。直到 JDK 1.7 版本该问题仍旧存在，没有被根本解决。

### I/O线程模型
目前存在的线程模型主要有：
- 传统阻塞I/O服务模型
- Reactor模式

根据Reactor的数量和处理资源池线程的数量不同，有如下3种典型的实现
- 单Reactor单线程
- 单Reactor多线程
- 主从Reactor多线程

Netty线程模型主要基于主从Reactor多线程模型做了一定的改进，其中主从Reactor多线程模型有多个Reactor。