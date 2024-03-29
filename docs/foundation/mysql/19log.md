---
title: MySQL MySQL日志
date: 2021-01-22
sidebar: auto
categories:
 - MySQL
tags:
- MySQL
prev: false
next: false
---

在任何一种数据库中，都会有各种各样的日志，记录着数据库工作的方方面面，以帮助数据库管理员追踪数据库曾经发生过的各种事件。MySQL也不例外，在MySQL中，有4种不同的日志，分别是错误日志、二进制日志（BINLOG 日志）、查询日志和慢查询日志，这些日志记录着数据库在不同方面的踪迹。本章将详细介绍这几种日志的作用和使用方法，希望读者能充分利用这些日志对数据库进行各种维护和调优。

## 错误日志

错误日志是MySQL中最重要的日志之一，它记录了当mysqld启动和停止时，以及服务器在运行过程中发生任何严重错误时的相关信息。当数据库出现任何故障导致无法正常使用时，可以首先查看此日志。


可以用--log-error=filename 选项来指定mysqld保存错误日志文件的位置。如果没有给定filename,mysqld使用错误日志名host_name.err并且默认在参数DATADIR(数据目录)指定的目录中写入日志文件。当然，一般情况下都是运维配置和启动的数据库，所以要找到错误日志文件一般可以
```sql
show variables like 'log_error' --查看错误日志文件路径
show variables like 'general_log_file' -- 查看日志文件路径
show variables like 'slow_query_log_file' --查看慢查询日志文件路径
```
结果如下：
```shell
mysql> show variables like 'slow_query_log_file';
+---------------------+-----------------------------------+
| Variable_name       | Value                             |
+---------------------+-----------------------------------+
| slow_query_log_file | /var/lib/mysql/localhost-slow.log |
+---------------------+-----------------------------------+
1 row in set (0.00 sec)

mysql> show variables like 'general_log_file';
+------------------+------------------------------+
| Variable_name    | Value                        |
+------------------+------------------------------+
| general_log_file | /var/lib/mysql/localhost.log |
+------------------+------------------------------+
1 row in set (0.00 sec)

mysql> show variables like 'log_error';
+---------------+---------------------+
| Variable_name | Value               |
+---------------+---------------------+
| log_error     | /var/log/mysqld.log |
+---------------+---------------------+
1 row in set (0.00 sec)
```

## 二进制日志文件

二进制日志（BINLOG）记录了所有的DDL语句和DML语句，但是不包括数据查询语句，如：select和show。语句以‘事件’的形式保存，它描述了数据的更改过程。此日志对于灾难时的数据恢复起着极其重要的作用。

二进制日志用--log-bin=filename 选项启动时，mysqld开始将数据变更情况写入日志文件。如果没有给出filename的值，默认名为主机名+‘-bin’,文件默认写在DATADIR指定的目录。
首先，需要查看二进制日志是否开启：
```sql
show variables like 'log_bin';
```
```shell

mysql> show variables like 'log_bin';
+---------------+-------+
| Variable_name | Value |
+---------------+-------+
| log_bin       | ON    |
+---------------+-------+
1 row in set (0.00 sec)

```
当为ON时，说明日志已开启。当日志开启时，我们要先知道二进制日志格式。分别有STATEMENT、ROW、MIXED这三种,查看当前数据库的日志格式：
```sql
show variables like 'binlog_format';
```
```shell
mysql> show variables like 'binlog_format';
+---------------+-------+
| Variable_name | Value |
+---------------+-------+
| binlog_format | ROW   |
+---------------+-------+
1 row in set (0.00 sec)
```
可以看出，在MySQL8.0之后，默认的二进制日志格式为ROW，下面详细学习日志格式的区别

### STATEMENT
顾名思义，日志中记录的都是语句（statement），每一条对数据造成修改的SQL语句都会记录在日志中，通过mysqlbinlog工具，可以清晰地看到每条语句的文本。
#### 优点
无需记录每行的变化，减少了binlog日志量，节约了IO，提高性能。
#### 缺点
由于只记录了执行语句，为了这些语句在slave上正确运行，因此还必须记录每条语句在执行时的一些相关信息，以保证所有语句能在slave得到和在master端执行时候的相同结果，另外像一些函数，存储过程等，slave要和master要保持一直会有很多相关问题。
### ROW
将每一行的变更记录都更新到日志中，而不是记录SQL语句。比如一个简单的更新：update table set name ='abc'，如果此语句影响全表数据，那么将会产生全表的数据变更日志。

#### 优点
binlog中可以不记录执行的sql语句的上下文相关的信息，仅需要记录那一条记录被修改成什么了。所以rowlevel的日志内容会非常清楚的记录下每一行数据修改的细节。而且不会出现某些特定情况下的存储过程，或function，以及trigger的调用和触发无法被正确复制的问题
#### 缺点
所有的执行的语句当记录到日志中的时候，都将以每行记录的修改来记录，这样可能会产生大量的日志内容,比如一条update语句，修改多条记录，则binlog中每一条修改都会有记录，这样造成binlog日志量会很大，特别是当执行alter table之类的语句的时候，由于表结构修改，每条记录都发生改变，那么该表每一条记录都会记录到日志中。

### MIXED
以上两种level的混合使用。 一般的语句修改使用statment格式保存binlog，如一些函数，statement无法完成主从复制的操作，则采用row格式保存binlog,MySQL会根据执行的每一条具体的sql语句来区分对待记录的日志形式，也就是在Statement和Row之间选择一种.新版本的MySQL中队row level模式也被做了优化，并不是所有的修改都会以row level来记录，像遇到表结构变更的时候就会以statement模式来记录。至于update或者delete等修改数据的语句，还是会记录所有行的变更。比如采用NDB存储引擎，此时对表的DML语句全部采用ROW；客户端使用了临时表；客户端采用了不确定函数，比如current_user()等，因为这种不确定函数在主从中得到的值可能不同，导致主从数据产生不一致。MIXED格式能尽量利用两种模式的优点，而避开它们的缺点。

:::warning
可以在global和session级别对binlog_format 进行日志格式的设置，但一定要谨慎操作，确保从库的复制能够正常进行。
:::


由于日志以二进制方式存储，不能直接读取，需要用mysqlbinlog工具来查看，语法如下：
```shell
##在未配置binlog路径的情况下，从配置文件中找到DATADIR，找到binlog日志文件
shell>mysqlbinlog binlog.000002
SET @@SESSION.GTID_NEXT= 'ANONYMOUS'/*!*/;
# at 5025
#220217 23:23:50 server id 1  end_log_pos 5359 CRC32 0x45fd4ad8         Query   thread_id=13    exec_time=0     error_code=0    Xid = 31
SET TIMESTAMP=1645111430/*!*/;
CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `dept_emp_latest_date` AS SELECT emp_no, MAX(from_date) AS from_date, MAX(to_date) AS to_date
    FROM dept_emp
    GROUP BY emp_no
/*!*/;
# at 5359
#220217 23:23:50 server id 1  end_log_pos 5438 CRC32 0x400bc7f9         Anonymous_GTID  last_committed=15       sequence_number=16 rbr_only=no      original_committed_timestamp=1645111430089545   immediate_commit_timestamp=1645111430089545     transaction_length=497
# original_commit_timestamp=1645111430089545 (2022-02-17 23:23:50.089545 CST)
# immediate_commit_timestamp=1645111430089545 (2022-02-17 23:23:50.089545 CST)
/*!80001 SET @@session.original_commit_timestamp=1645111430089545*//*!*/;
/*!80014 SET @@session.original_server_version=80028*//*!*/;
/*!80014 SET @@session.immediate_server_version=80028*//*!*/;
SET @@SESSION.GTID_NEXT= 'ANONYMOUS'/*!*/;
# at 5438
#220217 23:23:50 server id 1  end_log_pos 5856 CRC32 0x766a2536         Query   thread_id=13    exec_time=0     error_code=0    Xid = 32
SET TIMESTAMP=1645111430/*!*/;
CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `current_dept_emp` AS SELECT l.emp_no, dept_no, l.from_date, l.to_date
    FROM dept_emp d
        INNER JOIN dept_emp_latest_date l
        ON d.emp_no=l.emp_no AND d.from_date=l.from_date AND l.to_date = d.to_date
/*!*/;
# at 5856
#220217 23:23:50 server id 1  end_log_pos 5900 CRC32 0x6a45dfa9         Rotate to binlog.000002  pos: 4
SET @@SESSION.GTID_NEXT= 'AUTOMATIC' /* added by mysqlbinlog */ /*!*/;
DELIMITER ;
# End of log file
/*!50003 SET COMPLETION_TYPE=@OLD_COMPLETION_TYPE*/;
/*!50530 SET @@SESSION.PSEUDO_SLAVE_MODE=0*/;
```
如果日志为ROW格式，则解析后为一堆二进制字符，无法读懂
```shell
BINLOG 'EBq/ShMBAAAAPwAAAK4EAAAAADoAAAAAAAAABm1lbWJlcgACdDIACgMPDw/+CgsPAQwKJAAoAEAA/gJAAAAAEBq/ShgBAAAAtAAAAGIFAAAQADoAAAAAAAEACv////8A/AEAAAALYWxleDk5ODh5b3UEOXlvdSA3
```
此时可以加上-v或者-vv读取参数。

- 可以使用reset master命令进行日志删除。（该命令将删除所有binlog日志，新日志编号将从000001开始）
- PURGE MASTER LOGS TO 'mysql-bin.编号' 删除指定编号之前的所有日志。
- PURGE MASTER LOGS BEFORE 'yyyy-mm-dd hh24:mi:ss' 删除指定日期之前的所有日志
- 设置参数--expire_logs_days=#，此参数的含义是设置日志的过期天数，过了指定的天数后日志将会被自动删除，这样将有利于减少 DBA管理日志的工作量。


二进制日志文件由于记录了数据的变化过程，对于数据的完整性和安全性起着重要的作用。
因此，MySQL还提供了一些其他参数选项来进行更小粒度的管理，具体介绍如下。

--binlog-do-db=db_name：该选项告诉主服务器，如果当前的数据库(即USE选定的数据库)是db_name，应将更新记录到二进制日志中。其他所有没有显式指定的数据库更新将被忽略，不记录在日志中。--binlog-ignore-db=db_name：该选项告诉主服务器，如果当前的数据库（即USE选定的数据库）是db_name，不应将更新保存到二进制日志中，其他没有显式忽略的数据库都将进行记录。如果想记录或忽略多个数据库，可以对上面两个选项分别使用多次，即对每个数据库指定相应的选项。

例如，如果只想记录数据库db1和db2的日志，可以在参数文件中设置两行：

--binlog-do-db=db1--binlog-do-db=db2--innodb-safe-binlog：此选项经常和--sync-binlog＝N（每写N次日志同步磁盘）一起配合使用，使得事务在日志中的记录更加安全。SET SQL_LOG_BIN=0：具有SUPER权限的客户端可以通过此语句禁止将自己的语句记入二进制记录。这个选项在某些环境下是有用的，但是使用时一定要小心，因为它很可能造成日志记录的不完整或者在复制环境中造成主从数据的不一致
## 查询日志

查询日志和慢查询日志都可以选择保存在文件或者表中，并使用参数--log-output[=value,...]来进行控制，value值可以是TABLE、FILE、NONE的一个或者多个组合，中间用逗号进行分割，分别表示日志保存在表、文件、不保存在表和文件中，这里的表指的是mysql 库中的 general_log（慢查询日志是slow_log）表。其中 NONE 的优先级最高，比如--log-output =TABLE,FILE表示日志可以同时输出到表和文件中，而--log-output =TABLE,NONE由于NONE的优先级，表示日志不保存在表和文件中。如果不显式设置此参数，则默认输出到文件。在MySQL 5.5.7之前的版本中，日志记录到表比记录到文件要占用更多的系统资源，如果需要更高的性能，则建议使用文件来记录日志。

:::warning
log日志中记录了所有数据库的操作，对于访问频繁的系统，此日志对系统性能的影响较大，建议一般情况下关。
:::

### 慢查询日志
慢查询日志记录了所有执行时间超过参数long_query_time（单位：秒）设置值并且扫描记录数不小于min_examined_row_limit的所有SQL语句的日志（注意，获得表锁定的时间不算作执行时间）。long_query_time默认为10秒，最小为0，精度可以到微秒。

在默认情况下，有两类常见语句不会记录到慢查询日志：**管理语句和不使用索引进行查询的语句**。这里的管理语句包括 ALTER TABLE、ANALYZE TABLE、CHECKTABLE、CREATE INDEX、 DROP INDEX、OPTIMIZE TABLE和 REPAIR TABLE。如果要监控这两类SQL语句，可以分别通过参数--log-slow-admin-statements和log_queries_not_using_indexes进行控制。
```sql
mysql> show variables like 'log_queries_not_using_indexes';--查询是否记录未使用索引的查询
+-------------------------------+-------+
| Variable_name                 | Value |
+-------------------------------+-------+
| log_queries_not_using_indexes | OFF   |
+-------------------------------+-------+
1 row in set (0.00 sec)

mysql> set global log_queries_not_using_indexes='on';
Query OK, 0 rows affected (0.00 sec)

mysql> show variables like 'log_queries_not_using_indexes';
+-------------------------------+-------+
| Variable_name                 | Value |
+-------------------------------+-------+
| log_queries_not_using_indexes | ON    |
+-------------------------------+-------+
1 row in set (0.00 sec)


mysql> show variables like 'long%';--查询慢查询时间
+-----------------+-----------+
| Variable_name   | Value     |
+-----------------+-----------+
| long_query_time | 10.000000 |
+-----------------+-----------+
1 row in set (0.00 sec)

mysql> set long_query_time=2;
Query OK, 0 rows affected (0.00 sec)

mysql> show variables like 'long%';
+-----------------+----------+
| Variable_name   | Value    |
+-----------------+----------+
| long_query_time | 2.000000 |
+-----------------+----------+
1 row in set (0.00 sec)

```
如果慢查询日志中记录的内容很多，可以使用mysqlddumpslow工具来对慢查询日志进行分类汇总。
```shell
mysqldumpslow filename
Reading mysql slow query log from localhost-slow.log
Count: 1 Time=297.00s (297s) Lock=0.00s (0s) Rows=0.0 (0),root[root]@localhostselect count(N) from emp t1,emp t2 where t1.id<>t2.id
Count: 2 Time=11.00s (22s) Lock=0.00s (0s) Rows=1.0 (2),root[root]@localhostselect count(N) from emp t1,dept t2 where t1.id=t2.id
Count: 1 Time=9.00s (9s) Lock=0.00s (0s) Rows=0.0 (0),root[root]@localhostselect count(N) from emp t1,emp t2 where t1.id=t2.id
```
:::warning
慢查询日志对于我们发现应用中有性能问题的SQL很有帮助，建议正常情况下，打开此日志并经常查看分析。
:::


## mysqlsla
在很多情况下，都需要对MySQL日志进行各种分析，来了解系统运行的方方面面。比如我们可能需要对查询日志进行分析，看一下哪些语句执行最频繁，从而了解客户最关心的功能，哪些重要的功能为什么点击率低，需要我们进一步来完善；再比如 DBA 很关心性能问题，需要从慢查询日志中进行分析，找到最消耗时间的SQL进行优化，等等。
如前所述，MySQL自带了一些工具可以对日志进行分析，比如mysqlbinlog可以用来分析二进制日志，mysqlslow可以用来分析慢查询日志，但这些工具相对功能较单一，而且对查询日志没有提供分析工具。因此，很多第三方工具应用而生，而mysqlsla是其中使用较广泛的一个。