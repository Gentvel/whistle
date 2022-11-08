(window.webpackJsonp=window.webpackJsonp||[]).push([[57],{603:function(e,v,_){"use strict";_.r(v);var r=_(4),a=Object(r.a)({},(function(){var e=this,v=e.$createElement,_=e._self._c||v;return _("ContentSlotsDistributor",{attrs:{"slot-key":e.$parent.slotKey}},[_("h2",{attrs:{id:"一、简介"}},[_("a",{staticClass:"header-anchor",attrs:{href:"#一、简介"}},[e._v("#")]),e._v(" 一、简介")]),e._v(" "),_("p",[e._v("插件式存储引擎是MySQL数据库最重要的特性之一，用户可以根据应用的需要选择如何存储和索引数据、是否使用事务等。MySQL默认支持多种存储引擎，以适用于不同领域的数据库应用需要，用户可以通过选择使用不同的存储引擎提高应用的效率，提供灵活的存储，用户甚至可以按照自己的需要定制和使用自己的存储引擎，以实现最大程度的可定制性。")]),e._v(" "),_("p",[_("code",[e._v("show engines")]),e._v(" 查看当前数据库支持的引擎")]),e._v(" "),_("h2",{attrs:{id:"memory"}},[_("a",{staticClass:"header-anchor",attrs:{href:"#memory"}},[e._v("#")]),e._v(" MEMORY")]),e._v(" "),_("p",[e._v("缺点：不支持事务")]),e._v(" "),_("p",[e._v("使用MEMORY存储引擎的表，其数据存储在内存中，且行的长度固定，这两个特点使得MEMORY存储引擎非常快。")]),e._v(" "),_("p",[e._v("MEMORY存储引擎管理的表具有下列特征：")]),e._v(" "),_("p",[e._v("在数据库目录内，每个表均以.frm格式的文件表示\n表数据及索引被存储在内存中\n表级锁机制\n不能包含TEXT或BLOB字段\nMEMORY存储引擎以前被称为HEAP引擎")]),e._v(" "),_("h2",{attrs:{id:"myisam"}},[_("a",{staticClass:"header-anchor",attrs:{href:"#myisam"}},[e._v("#")]),e._v(" MyISAM")]),e._v(" "),_("p",[e._v("mysql 5.5 之前的默认引擎\n这种存储引擎是mysql最常用的存储引擎")]),e._v(" "),_("p",[e._v("它管理的表具有以下特征：")]),e._v(" "),_("p",[e._v("使用三个文件表示每个表\n格式文件 — 存储表结构的定义（xxx.frm）\n数据文件 — 存储表行的内容（xxx.MYD）\n索引文件 — 存储表上索引（xxx.MYI）\n灵活的AUTO_INCREMENT字段处理\n可被转换压缩，只读表来节省空间")]),e._v(" "),_("p",[e._v("要指定索引文件和数据文件的路径，需要在创建表的时候通过 DATA DIRECTORY和INDEX DIRECTORY语句指定，也就是说不同MyISAM表的索引文件和数据文件可以放置到不同的路径下。文件路径需要是绝对路径，并且具有访问权限。")]),e._v(" "),_("p",[e._v("MyISAM类型的表可能会损坏，原因可能是多种多样的，损坏后的表可能不能被访问，会提示需要修复或者访问后返回错误的结果。MyISAM类型的表提供修复的工具，可以用CHECK TABLE语句来检查MyISAM表的健康，并用 "),_("code",[e._v("REPAIR TABLE")]),e._v("语句修复一个损坏的MyISAM表。表损坏可能导致数据库异常重新启动，需要尽快修复并尽可能地确认损坏的原因。")]),e._v(" "),_("h2",{attrs:{id:"mrg-myisam"}},[_("a",{staticClass:"header-anchor",attrs:{href:"#mrg-myisam"}},[e._v("#")]),e._v(" MRG_MYISAM")]),e._v(" "),_("p",[e._v("MRG_MYISAM也叫Merge存储引擎，是一组MyIsam的组合，也就是说，他将MyIsam引擎的多个表聚合起来，但是他的内部没有数据，真正的数据依然是MyIsam引擎的表中，但是可以直接进行查询、删除更新等操作。")]),e._v(" "),_("p",[e._v("可以直接从数据表里面操作， 也可以直接在mrg表里面， 删除mrg表， 不会影响实际表的数据。")]),e._v(" "),_("p",[e._v("MyISAM的表还支持3种不同的存储格式，分别是：静态（固定长度）表；动态表；压缩表。")]),e._v(" "),_("p",[e._v("其中，静态表是默认的存储格式。静态表中的字段都是非变长字段，这样每个记录都是固定长度的，这种存储方式的优点是存储非常迅速，容易缓存，出现故障容易恢复；缺点是占用的空间通常比动态表多。静态表的数据在存储时会按照列的宽度定义补足空格，但是在应用访问的时候并不会得到这些空格，这些空格在返回给应用之前已经去掉。")]),e._v(" "),_("p",[e._v("动态表中包含变长字段，记录不是固定长度的，这样存储的优点是占用的空间相对较少，但是频繁地更新和删除记录会产生碎片，需要定期执行"),_("code",[e._v("OPTIMIZE TABLE")]),e._v("语句或myisamchk-r命令来改善性能，并且在出现故障时恢复相对比较困难。")]),e._v(" "),_("p",[e._v("压缩表由myisampack工具创建，占据非常小的磁盘空间。因为每个记录是被单独压缩的，所以只有非常小的访问开支。")]),e._v(" "),_("h2",{attrs:{id:"innodb"}},[_("a",{staticClass:"header-anchor",attrs:{href:"#innodb"}},[e._v("#")]),e._v(" InnoDB")]),e._v(" "),_("p",[e._v("mysql 5.5 之后的默认引擎")]),e._v(" "),_("p",[e._v("它管理的表具有下列主要特征：")]),e._v(" "),_("ul",[_("li",[e._v("每个InnoDB表在数据库目录中以.frm格式文件表示")]),e._v(" "),_("li",[e._v("InnoDB表空间 tablespace 被用来存储表的内容")]),e._v(" "),_("li",[e._v("提供一组用来记录事务性活动的日志文件")]),e._v(" "),_("li",[e._v("用COMMIT(提交)，SAVEPOINT及ROLLBACK(回滚)支持事物处理")]),e._v(" "),_("li",[e._v("提供全ACID兼容")]),e._v(" "),_("li",[e._v("在mysql服务器崩溃后提供自动恢复")]),e._v(" "),_("li",[e._v("多版本（MVCC）和行级锁定")]),e._v(" "),_("li",[e._v("支持外键及引用的完整性，包括级联更新和删除")])]),e._v(" "),_("h2",{attrs:{id:"archive"}},[_("a",{staticClass:"header-anchor",attrs:{href:"#archive"}},[e._v("#")]),e._v(" ARCHIVE")]),e._v(" "),_("p",[e._v("从archive单词的解释我们大概可以明白这个存储引擎的用途，这个存储引擎基本上用于数据归档；它的压缩比非常的高，存储空间大概是innodb的10-15分之一所以它用来存储历史数据非常的适合，由于它不支持索引同时也不能缓存索引和数据，所以它不适合作为并发访问表的存储引擎。Archivec存储引擎使用行锁来实现高并发插入操作，但是它不支持事务，其设计目标只是提供高速的插入和压缩功能。\n每个archive表在磁盘上存在两个文件")]),e._v(" "),_("p",[e._v(".frm(存储表定义)")]),e._v(" "),_("p",[e._v(".arz(存储数据)")]),e._v(" "),_("ol",[_("li",[e._v("archive存储引擎支持insert、replace和select操作，但是不支持update和delete。")]),e._v(" "),_("li",[e._v("archive存储引擎支持blob、text等大字段类型。支持auto_increment自增列同时自增列可以不是唯一索引。")]),e._v(" "),_("li",[e._v("archive支持auto_increment列，但是不支持往auto_increment列插入一个小于当前最大的值的值。")]),e._v(" "),_("li",[e._v("archive不支持索引所以无法在archive表上创建主键、唯一索引、和一般的索引。")])]),e._v(" "),_("h2",{attrs:{id:"csv"}},[_("a",{staticClass:"header-anchor",attrs:{href:"#csv"}},[e._v("#")]),e._v(" CSV")]),e._v(" "),_("p",[e._v("CSV存储引擎可以将csv文件作为mysql的表进行处理。存储格式就是普通的csv文件。\n文件系统存储特点\n数据以文本方式存储在文件中（Innodb则是二进制）")]),e._v(" "),_("ul",[_("li",[e._v(". CSV文件存储表内容")]),e._v(" "),_("li",[e._v(".CSM文件存储表的元数据如表状态和数据量")]),e._v(" "),_("li",[e._v(".frm文件存储表结构信息")])]),e._v(" "),_("p",[e._v("CSV特点")]),e._v(" "),_("ul",[_("li",[e._v("以CSV格式进行数据存储（逗号隔开，引号）")]),e._v(" "),_("li",[e._v("所有的列必须都是不能为NULL的")]),e._v(" "),_("li",[e._v("不支持索引（不适合大表，不适合在线处理）")]),e._v(" "),_("li",[e._v("可以对数据文件直接编辑（保存文本文件内容）")])]),e._v(" "),_("h2",{attrs:{id:"federated"}},[_("a",{staticClass:"header-anchor",attrs:{href:"#federated"}},[e._v("#")]),e._v(" FEDERATED")]),e._v(" "),_("p",[e._v("FEDERATED存储引擎访问在远程数据库的表中的数据，而不是本地的表。这个特性给某些开发应用带来了便利，你可以直接在本地构建一个federated表来连接远程数据表，配置好了之后本地表的数据可以直接跟远程数据表同步。实际上这个引擎里面是不真实存放数据的，所需要的数据都是连接到其他MySQL服务器上获取。")]),e._v(" "),_("h2",{attrs:{id:"performance-schema"}},[_("a",{staticClass:"header-anchor",attrs:{href:"#performance-schema"}},[e._v("#")]),e._v(" PERFORMANCE_SCHEMA")]),e._v(" "),_("p",[e._v("MySQL的performance schema 用于监控MySQL server在一个较低级别的运行过程中的资源消耗、资源等待等情况，它具有以下特点：")]),e._v(" "),_("p",[e._v("a、提供了一种在数据库运行时实时检查server的内部执行情况的方法。performance_schema 数据库中的表使用performance_schema存储引擎。该数据库主要关注数据库运行过程中的性能相关的数据，与information_schema不同，information_schema主要关注server运行过程中的元数据信息。")]),e._v(" "),_("p",[e._v("b、performance_schema通过监视server的事件来实现监视server内部运行情况， “事件”就是server内部活动中所做的任何事情以及对应的时间消耗，利用这些信息来判断server中的相关资源消耗在了哪里?一般来说，事件可以是函数调用、操作系统的等待、SQL语句执行的阶段(如sql语句执行过程中的parsing 或 sorting阶段)或者整个SQL语句与SQL语句集合。事件的采集可以方便的提供server中的相关存储引擎对磁盘文件、表I/O、表锁等资源的同步调用信息。")]),e._v(" "),_("p",[e._v("c、performance_schema中的事件与写入二进制日志中的事件(描述数据修改的events)、事件计划调度程序(这是一种存储程序)的事件不同。performance_schema中的事件记录的是server执行某些活动对某些资源的消耗、耗时、这些活动执行的次数等情况。")]),e._v(" "),_("p",[e._v("d、performance_schema中的事件只记录在本地server的performance_schema中，其下的这些表中数据发生变化时不会被写入binlog中，也不会通过复制机制被复制到其他server中。")]),e._v(" "),_("p",[e._v("e、当前活跃事件、历史事件和事件摘要相关的表中记录的信息。能提供某个事件的执行次数、使用时长。进而可用于分析某个特定线程、特定对象(如mutex或file)相关联的活动。")]),e._v(" "),_("p",[e._v("f、performance_schema存储引擎使用server源代码中的“检测点”来实现事件数据的收集。对于performance_schema实现机制本身的代码没有相关的单独线程来检测，这与其他功能(如复制或事件计划程序)不同。")]),e._v(" "),_("p",[e._v("g、收集的事件数据存储在performance_schema数据库的表中。这些表可以使用SELECT语句查询，也可以使用SQL语句更新performance_schema数据库中的表记录(如动态修改performance_schema的setup_*开头的几个配置表，但要注意：配置表的更改会立即生效，这会影响数据收集)。")]),e._v(" "),_("p",[e._v("h、performance_schema的表中的数据不会持久化存储在磁盘中，而是保存在内存中，一旦服务器重启，这些数据会丢失(包括配置表在内的整个performance_schema下的所有数据)。")]),e._v(" "),_("p",[e._v("i、MySQL支持的所有平台中事件监控功能都可用，但不同平台中用于统计事件时间开销的计时器类型可能会有所差异。")]),e._v(" "),_("h2",{attrs:{id:"blackhole"}},[_("a",{staticClass:"header-anchor",attrs:{href:"#blackhole"}},[e._v("#")]),e._v(" BLACKHOLE")]),e._v(" "),_("p",[e._v("黑洞引擎，写入的任何数据都会消失，用于记录binlog做复制的中继存储！")]),e._v(" "),_("h2",{attrs:{id:"tokudb"}},[_("a",{staticClass:"header-anchor",attrs:{href:"#tokudb"}},[e._v("#")]),e._v(" TokuDB")]),e._v(" "),_("p",[e._v("前面介绍的都是MySQL自带的存储引擎，除了这些之外，还有一些常见的第三方存储引擎，在某些特定应用中也有广泛使用，比如列式存储引擎Infobright、高写性能高压缩的TokuDB就是其中非常有代表性的两种")]),e._v(" "),_("p",[e._v("TokuDB是一个高性能、支持事务处理的MySQL和MariaDB的存储引擎，具有高扩展性、高压缩率、高效的写入性能，支持大多数在线DDL操作。最新版本已经开源，可以从Tokutek官方网站中进行"),_("a",{attrs:{href:"http://www.tokutek.com/products/downloads/",target:"_blank",rel:"noopener noreferrer"}},[e._v("下载和安装"),_("OutboundLink")],1)]),e._v(" "),_("h2",{attrs:{id:"几种常用存储引擎的适用环境"}},[_("a",{staticClass:"header-anchor",attrs:{href:"#几种常用存储引擎的适用环境"}},[e._v("#")]),e._v(" 几种常用存储引擎的适用环境")]),e._v(" "),_("ul",[_("li",[e._v("MyISAM：默认的MySQL插件式存储引擎。如果应用是以读操作和插入操作为主，只有很少的更新和删除操作，并且对事务的完整性、并发性要求不是很高，那么选择这个存储引擎是非常适合的。MyISAM是在Web、数据仓储和其他应用环境下最常使用的存储引擎之一。")]),e._v(" "),_("li",[e._v("InnoDB：用于事务处理应用程序，支持外键。如果应用对事务的完整性有比较高的要求，在并发条件下要求数据的一致性，数据操作除了插入和查询以外，还包括很多的更新、删除操作，那么InnoDB存储引擎应该是比较合适的选择。InnoDB存储引擎除了有效地降低由于删除和更新导致的锁定，还可以确保事务的完整提交（Commit）和回滚（Rollback），对于类似计费系统或者财务系统等对数据准确性要求比较高的系统，InnoDB都是合适的选择。")]),e._v(" "),_("li",[e._v("MEMORY：将所有数据保存在 RAM 中，在需要快速定位记录和其他类似数据的环境下，可提供极快的访问。MEMORY 的缺陷是对表的大小有限制，太大的表无法缓存在内存中，其次是要确保表的数据可以恢复，数据库异常终止后表中的数据是可以恢复的。MEMORY表通常用于更新不太频繁的小表，用以快速得到访问结果。")]),e._v(" "),_("li",[e._v("MERGE：用于将一系列等同的MyISAM表以逻辑方式组合在一起，并作为一个对象引用它们。MERGE表的优点在于可以突破对单个MyISAM表大小的限制,并且通过将不同的表分布在多个磁盘上，可以有效地改善MERGE表的访问效率。这对于诸如数据仓储等VLDB环境十分适合。")])])])}),[],!1,null,null,null);v.default=a.exports}}]);