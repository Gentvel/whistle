---
title: 【Java基础】系列六 IO流
date: 2020-08-05
sidebar: auto
categories:
 - java
tags:
- javase
prev: ./exception
next: ./thread
---

## 一、概述
Java Io 流共涉及 40 多个类，这些类看上去很杂乱，但实际上很有规则，而且彼此之间存在非常紧密的联系， Java I0 流的 40 多个类都是从如下 4 个抽象类基类中派生出来的。

InputStream/Reader: 所有的输入流的基类，前者是字节输入流，后者是字符输入流。
OutputStream/Writer: 所有输出流的基类，前者是字节输出流，后者是字符输出流。

<center>

![CACHE](./img/io.png)

</center>
