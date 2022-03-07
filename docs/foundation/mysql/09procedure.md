---
title: 存储过程与函数
date: 2021-01-22
sidebar: auto
categories:
 - MySQL
tags:
- MySQL
prev: false
next: false
---

在MySQL中，创建存储过程和函数使用的语句分别是CREATE PROCEDURE和CREATE FUNCTION。使用CALL语句来调用存储过程，只能用输出变量返回值。函数可以从语句外调用（引用函数名），也能返回标量值。存储过程也可以调用其他存储过程。
