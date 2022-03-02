---
title: MySQL 字符集
date: 2021-01-22
sidebar: auto
categories:
 - MySQL
tags:
- MySQL
prev: false
next: false
---

## 字符集简介

计算机只能存储二进制数据，所以存储字符串时，需要建立字符和二进制数据的映射关系。将一个字符映射成一个二进制数据的过程叫做**编码**；将一个二进制数据映射到一个字符的过程叫做**解码**。

人们用**字符集**的概念来描述某个字符范围的编码规则。(unicode)

使用`show charset`可查看MySQL支持的字符集

## 比较规则简介

当确定了字符集表示的字符范围以及编码规则后，需要比较两个字符的大小，比方说字符 'a' 的编码为 0x01 ，字符 'b' 的编码为 0x02 ，所以 'a' 小于 'b' ，这种简单的比较规则也可以被称为二进制比较规则，英文名为 binary collation。

二进制比较规则是简单，但有时候并不符合现实需求，比如在很多场合对于英文字符我们都是不区分大小写的，也就是说 'a' 和 'A' 是相等的，在这种场合下就不能简单粗暴的使用二进制比较规则了，这时候我们可以这样指定比较规则：

1. 将两个大小写不同的字符全部转为大写或者小写。
2. 比较这两个字符对应的二进制数据。

使用`show collation`可查看MySQL支持的比较规则，通常，MySQL默认的utf8字符集为utf8_general_ci

比较规则的命名规则如下：

- 比较规则名称和关联的字符集名称开头。
- 后边紧跟着的是比较规则主要作用于哪种语言。比如：utf8_polish_ci表示波兰语的规则比较，utf8_general_ci是一种通用的比较规则。
- 名称后缀意味着该比较规则是否区分语言中的重音和大小写。比如：_ai（accent insensitive）不区分重音,_as(accent sensitive) 区分重音；_ci(cese insensitive)不区分大小写,_cs(case sensitive)区分大小写 

## 一些重要的字符集

- ASCII字符集

    收录供128个字符，包括空格、标点符号、数字、大小写字母和一些不可见字符。

- ISO 8859-1 字符集

    收录256个字符，是在 ASCII 字符集的基础上又扩充了128个西欧常用字符(包括德法两国的字母)

- GB2312 字符集

    收录了汉字以及拉丁字母、希腊字母、日文平假名及片假名字母、俄语西里尔字母。其中收录汉字6763个，其他文字符号682个。同时这种字符集又兼容 ASCII 字符集，所以在编码方式上显得有些奇怪：如果该字符在 ASCII 字符集中，则采用1字节编码。否则采用2字节编码。

- GBK 字符集

    GBK 字符集只是在收录字符范围上对 GB2312 字符集作了扩充，编码方式上兼容 GB2312 。

- utf8 字符集

    收录地球上能想到的所有字符，而且还在不断扩充。这种字符集兼容 ASCII 字符集，采用变长编码方式，编码一个字符需要使用1～4个字节

## UTF8和UTF8mb4

utf8 字符集 表示一个字符需要使用1~4个字节，但是我们常用的一些字符使用1-3个字节就可以表示。而在MySQL中，字符集表示一个字符所用最大字节长度在某些方面会影响系统的存储和性能，所以设计了两个概念：
    1.utf8(也就是ut8mb3):阉割过的utf8字符集，只使用1-3个字节表示字符。
    2.utf8mb4: 正宗的utf8字符集，使用1-4个字节表示字符。

通常，如果需要存储一些emoji表情啥的，就得使用utf8mb4。


## 字符集和比较规则的应用

MySQL有4个级别的字符姐和比较规则：服务器级别、数据库级别、表级别、行级别

### 服务器级别

```sql
mysql> show variables like 'character_set_server';
+----------------------+---------+
| Variable_name        | Value   |
+----------------------+---------+
| character_set_server | utf8mb4 |
+----------------------+---------+
1 row in set (0.01 sec)

mysql> show variables like 'collation_server';
+------------------+--------------------+
| Variable_name    | Value              |
+------------------+--------------------+
| collation_server | utf8mb4_0900_ai_ci |
+------------------+--------------------+
1 row in set (0.00 sec)

```

可以在MySQL服务启动时配置

```cnf
[server]
character_set_server=gbk
collation_server=gbk_chinese_ci
```

### 数据库级别

```sql
CREATE DATABASE 数据库名
 [[DEFAULT] CHARACTER SET 字符集名称]
 [[DEFAULT] COLLATE 比较规则名称];
ALTER DATABASE 数据库名
 [[DEFAULT] CHARACTER SET 字符集名称]
 [[DEFAULT] COLLATE 比较规则名称];

```

当然，如果在创建数据库的时候不指定字符集和比较规则，则会默认使用服务级别的字符集和比较规则

### 表级别

```sql
CREATE TABLE 表名 (列的信息)
 [[DEFAULT] CHARACTER SET 字符集名称]
 [COLLATE 比较规则名称]]
ALTER TABLE 表名
 [[DEFAULT] CHARACTER SET 字符集名称]
 [COLLATE 比较规则名称]
```

如果在创建和修改表的时候没有指明字符集和比较规则，则会使用该表所在的数据库的字符集和比较规则作为该表的默认选择。

### 列级别

```sql
CREATE TABLE 表名(
 列名 字符串类型 [CHARACTER SET 字符集名称] [COLLATE 比较规则名称],
 其他列...
);
```

如果在创建和修改的语句中没有指明字符集和比较规则，将使用该列所在表的字符集和比较规则作为该列的字符集和比较规则。

:::tip
由于字符集和比较规则是互相有联系的，如果我们只修改了字符集，比较规则也会跟着变化，如果只改变了比较规则，字符集也会跟着变化。

- 只修改字符集，则比较规则将变为修改后的字符集的默认比较规则。
- 只修改比较规则，则字符集将变为修改后的比较规则对应的字符集。
:::

## 客户端和服务器通信中的字符集

如果客户端和服务端用的字符集不同，那么，在客户端发送请求的时候，将某个字符编码变成二进制后发送到服务器，服务器以不同的字符集解码成字符串时就会出现解码成不同字符的问题。将会产生意向不到的结果。

其实，客户端发送到服务端的请求本质上就是一个字符串，服务器返回客户端的结果也是一个字符串，而字符串其实是某种字符集编码的二进制数据。这个字符串可不是使用一种字符集的编码方式就解决整个请求-响应的。MySQL从请求到返回这个过程伴随着多次的字符集转换。

```sql
mysql> show variables like 'character_set_client';
+----------------------+---------+
| Variable_name        | Value   |
+----------------------+---------+
| character_set_client | utf8mb4 |
+----------------------+---------+
1 row in set (0.00 sec)

mysql> show variables like 'character_set_connection';
+--------------------------+---------+
| Variable_name            | Value   |
+--------------------------+---------+
| character_set_connection | utf8mb4 |
+--------------------------+---------+
1 row in set (0.00 sec)

mysql> show variables like 'character_set_results';
+-----------------------+---------+
| Variable_name         | Value   |
+-----------------------+---------+
| character_set_results | utf8mb4 |
+-----------------------+---------+
1 row in set (0.00 sec)

```

通常都把 character_set_client 、character_set_connection、character_set_results 这三个系统变量设置成和客户端使用的字符集一致的情况，这样减少了很多无谓的字符集转换`SET NAMES 字符集名;`
