---
title: 【多线程】 篇 CAS(Compare And Swap)
date: 2020-08-23
sidebar: auto
categories:
- java
tags:
- thread
prev: false
next: false
---

## 一、介绍
CAS(Compare And Swap) 比较交换。Doug lea 在同步组件中大量使用cas技术鬼斧神工地实现java多线程并发操作。整个AQS同步组件，Atomic原子类操作等等都是以CAS实现的。
CAS交换比较过程（V.A.B）
- V.一个内存地址存放的实际值。
- A.旧的预期值。
- B.即将更新的值。

**当且仅当预期值A和内存值V相同时，将内存值修改为B并返回true，否则什么都不做，并返回false。**

### 1.1 CAS VS synchronized
synchronized 线程获取锁是一种悲观策略，即假设每一次执行临界区代码都会产生冲突，所以当前线程获取到锁的之后会阻塞其他线程获取该锁。
CAS(无锁操作) 获取锁是一种乐观策略，假设所有线程访问共享资源的时候不会出现冲突，所以出现冲突时就不会阻塞其他线程的操作，而是重试当前操作直到没有冲突为止。



