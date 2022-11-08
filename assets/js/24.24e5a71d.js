(window.webpackJsonp=window.webpackJsonp||[]).push([[24],{509:function(t,s,a){t.exports=a.p+"assets/img/mysqllevel.a1b163ee.png"},510:function(t,s,a){t.exports=a.p+"assets/img/20201022172148013.c24839e6.png"},623:function(t,s,a){"use strict";a.r(s);var e=a(4),r=Object(e.a)({},(function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("ContentSlotsDistributor",{attrs:{"slot-key":t.$parent.slotKey}},[e("h2",{attrs:{id:"一、mysql分层"}},[e("a",{staticClass:"header-anchor",attrs:{href:"#一、mysql分层"}},[t._v("#")]),t._v(" 一、mysql分层")]),t._v(" "),e("p",[t._v("MySQL服务端分为几个层如下：")]),t._v(" "),e("center",[e("p",[e("img",{attrs:{src:a(509),alt:"分层"}})])]),t._v(" "),e("h3",{attrs:{id:"_1-1-连接层"}},[e("a",{staticClass:"header-anchor",attrs:{href:"#_1-1-连接层"}},[t._v("#")]),t._v(" 1.1 连接层")]),t._v(" "),e("p",[t._v("提供与客户端连接的服务。\n当客户端发出一个请求后（如增删改查的SQL语句），首先到达该层，将服务器与客户端建立连接。")]),t._v(" "),e("h3",{attrs:{id:"_1-2-服务层"}},[e("a",{staticClass:"header-anchor",attrs:{href:"#_1-2-服务层"}},[t._v("#")]),t._v(" 1.2 服务层")]),t._v(" "),e("p",[t._v("服务层分两个作用：")]),t._v(" "),e("ul",[e("li",[t._v("提供各种用户使用的接口。 如select、insert等")]),t._v(" "),e("li",[t._v("提供SQL优化器（MySQL Query Optimizer）。 SQL优化器是MySQL服务层自带的一个服务，它会自动优化用户写得不是最优的SQL，使其达到优化的效果。但由于优化器毕竟只是优化器，有时候会将用户自定义的优化方案给更改掉，从而使用户自己的优化方案失效，这一点需要注意。")])]),t._v(" "),e("h3",{attrs:{id:"_1-3-引擎层"}},[e("a",{staticClass:"header-anchor",attrs:{href:"#_1-3-引擎层"}},[t._v("#")]),t._v(" 1.3 引擎层")]),t._v(" "),e("p",[t._v("引擎层提供各种数据存储的方式。MySQL的存储引擎有很多，比较常用的比如有InnoDB, MyISAM。")]),t._v(" "),e("p",[t._v("InnoDB与MyISAM的区别为：")]),t._v(" "),e("ul",[e("li",[t._v("InnoDB 事务优先，所以适合高并发操作，使用的是行锁")]),t._v(" "),e("li",[t._v("MyISAM 性能优先，适合查询多的场景，使用的是表锁")])]),t._v(" "),e("h3",{attrs:{id:"_1-4-存储层"}},[e("a",{staticClass:"header-anchor",attrs:{href:"#_1-4-存储层"}},[t._v("#")]),t._v(" 1.4 存储层")]),t._v(" "),e("p",[t._v("最终的数据存储在存储层。")]),t._v(" "),e("h2",{attrs:{id:"二、存储引擎"}},[e("a",{staticClass:"header-anchor",attrs:{href:"#二、存储引擎"}},[t._v("#")]),t._v(" 二、存储引擎")]),t._v(" "),e("p",[t._v("查询数据库支持引擎："),e("code",[t._v("show engines;")])]),t._v(" "),e("center",[e("p",[e("img",{attrs:{src:a(510),alt:"支持引擎"}})])]),t._v(" "),e("p",[t._v("指定数据库对象的引擎，比如某张表需要使用某引擎")]),t._v(" "),e("div",{staticClass:"language-sql extra-class"},[e("pre",{pre:!0,attrs:{class:"language-sql"}},[e("code",[e("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("create")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("table")]),t._v(" tb"),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),t._v("\n    id "),e("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("int")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("auto_increment")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v("\n    name "),e("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("varchar")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),e("span",{pre:!0,attrs:{class:"token number"}},[t._v("32")]),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),t._v("\n"),e("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("ENGINE")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),t._v(" MyISAM "),e("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("AUTO_INCREMENT")]),e("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),e("span",{pre:!0,attrs:{class:"token number"}},[t._v("1")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("DEFAULT")]),t._v(" "),e("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("CHARSET")]),e("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),t._v("utf"),e("span",{pre:!0,attrs:{class:"token operator"}},[t._v("-")]),e("span",{pre:!0,attrs:{class:"token number"}},[t._v("8")]),t._v("\n")])])])],1)}),[],!1,null,null,null);s.default=r.exports}}]);