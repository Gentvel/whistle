---
title: 【多线程】  关键字
date: 2020-08-26
sidebar: auto
prev: false
next: false
---

## 一、volatile

### 1.1 实现原理
**volatile保证有序性原理**  
JMM通过插入内存屏障指令来禁止特定类型的重排序。  
java编译器在生成字节码时，在volatile变量操作前后的指令序列中插入内存屏障来特定类型的重排序

volatile内存屏障插入策略：
- 在每个volatile写操作的前面插入一个StoreStore屏障。
- 在每个volatile写操作的后面插入一个StoreLoad屏障。
- 在每个volatile读操作的后面插入一个LoadLoad屏障。
- 在每个volatile读操作的后面插入一个LoadStore屏障。

:::tip 
Store：数据对其他处理器可见（即：刷新到内存中）
Load：让缓存中的数据失效，重新从主内存加载数据
:::

**volatile保证可见性原理**

volatile内存屏障插入策略中有一条，“在每个volatile写操作的后面插入一个StoreLoad屏障”。

StoreLoad屏障会生成一个Lock前缀的指令，Lock前缀的指令在多核处理器下会引发了两件事：
1. 将当前处理器缓存行的数据写回到系统内存。
2. 这个写回内存的操作会使在其他CPU里缓存了该内存地址的数据无效。


volatile内存可见的写-读过程：

- volatile修饰的变量进行写操作。

- 由于编译期间JMM插入一个StoreLoad内存屏障，JVM就会向处理器发送一条Lock前缀的指令。

- Lock前缀的指令将该变量所在缓存行的数据写回到主内存中，并使其他处理器中缓存了该变量内存地址的数据失效。

- 当其他线程读取volatile修饰的变量时，本地内存中的缓存失效，就会到到主内存中读取最新的数据。

### 1.2 保证可见性
volatile保证了不同线程对volatile修饰变量进行操作时的可见性。
由原理可知，对于一个volatile变量的读，总是能看到对这个volatile变量最后的写入。
