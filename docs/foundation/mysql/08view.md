---
title: MySQL 视图
date: 2021-01-22
sidebar: auto
categories:
 - MySQL
tags:
- MySQL
prev: false
next: false
---

视图（View）是一种虚拟存在的表，对于使用视图的用户来说基本上是透明的。视图并不在数据库中实际存在，行和列数据来自定义视图的查询中使用的表，并且是在使用视图时动态生成的。视图相对于普通的表的优势主要包括以下几项:

- 简单：使用视图的用户完全不需要关心后面对应的表的结构、关联条件和筛选条件，对用户来说已经是过滤好的复合条件的结果集。
- 安全：使用视图的用户只能访问他们被允许查询的结果集，对表的权限管理并不能限制到某个行某个列，但是通过视图就可以简单地实现。
- 数据独立：一旦视图的结构确定了，可以屏蔽表结构变化对用户的影响，源表增加列对视图没有影响；源表修改列名，则可以通过修改视图来解决，不会造成对访问者的影响。

## 创建/修改视图

创建视图需要有CREATE VIEW的权限，并且对于查询涉及的列有SELECT权限。如果使用CREATE OR REPLACE或者ALTER修改视图，那么还需要该视图的DROP权限。
创建语法为：

```sql
--创建语法
CREATE [OR REPALCE]
 [ALGORATHM={UNDIFINED|MERGE|TEMPTABLE}]
 [DEFINER={user|current_user}]
 [SQL SECURITY {DEFINER |INVOKER}]
VIEW view_name[(column_list)]
AS select_statement
[WITH [CASCADED|LOCAL] CHECK OPTION]
--修改语法
ALTER [ALGORITHM ={UNDIFINED |MERGE | TEMPTABLE}]
VIEW view_name[(column_list)]
AS select_statement
[WITH [CASCADED|LOCAL] CHECK OPTION]
```

- **`OR REPLACE`**:表示在创建视图时会替换已有的视图
- **`ALGORATHM`**:视图算法（缺省ALGORITHM选项等同于ALGORITHM = UNDEFINED）
1.***UNDEFINED***：MySQL将自动选择所要使用的算法（如果可能，它倾向于MERGE而不是TEMPTABLE，这是因为MERGE通常更有效，而且如果使用了临时表，视图是不可更新的。）
2.***MERGE***：将视图的语句与视图定义合并起来，使得视图定义的某一部分取代语句的对应部分（每当执行的时候,先将视图的sql语句与外部查询视图的sql语句,合并在一起,最终执行；）
3.***TEMPTABLE***：将视图的结果存入临时表，然后使用临时表执行语句（使用了临时表，视图是不可更新的，）

- **`DEFINER`**：视图的创建者或定义者（指定关键字CURRENT_USER(当前用户)和不指定该选项效果相同）
- **`SQL SECURITY`**:要查询一个视图，首先必须要具有对视图的select权限，如果同一个用户对于视图所访问的表没有select权限就需要指定此选项（缺省SQL SECURITY选项等同于SQL SECURITY DEFINER）
1.***SQL SECURITY DEFINER***：定义(创建)视图的用户必须对视图所访问的表具有select权限，也就是说将来其他用户访问视图的时候以定义者的身份，可以查询到基表内容
2.***SQL SECURITY INVOKER***：访问视图的用户必须对视图所访问的表具有select权限。
- **`WITH CHECK OPTION`**:对于可以执行DML操作的视图，对视图所做的DML操作的结果，不能违反视图的WHERE条件的限制。(不指定选项则默认是CASCADED)
***CASCADED***：检查所有的视图，会检查嵌套视图及其底层的视图
***LOCAL***：只检查将要更新的视图本身，嵌套视图不检查其底层的视图

:::tip
MySQL视图的定义有一些限制，例如，在FROM关键字后面不能包含子查询，这和其他数据库是不同的，如果视图是从其他数据库迁移过来的，那么可能需要因此做一些改动，可以将子查询的内容先定义成一个视图，然后对该视图再创建视图就可以实现类似的功能了。
:::

视图的可更新性和视图中查询的定义有关系，以下类型的视图是不可更新的

- 包含以下关键字的SQL语句：聚合函数（SUM、MIN、MAX、COUNT等）、DISTINCT、GROUP BY、HAVING、UNION或者UNION ALL。
- 常量视图。
- SELECT中包含子查询。
- JOIN。
- FROM一个不能更新的视图。
- WHERE字句的子查询引用了FROM字句中的表
