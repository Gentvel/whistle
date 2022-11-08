(window.webpackJsonp=window.webpackJsonp||[]).push([[83],{634:function(t,s,a){"use strict";a.r(s);var n=a(4),e=Object(n.a)({},(function(){var t=this,s=t.$createElement,a=t._self._c||s;return a("ContentSlotsDistributor",{attrs:{"slot-key":t.$parent.slotKey}},[a("h2",{attrs:{id:"简介"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#简介"}},[t._v("#")]),t._v(" 简介")]),t._v(" "),a("p",[t._v("在之前的主从复中，提到了当master宕机下线后，会找一个slave作为master继续执行，但是，由谁来确认master宕机呢？怎么找slave，找哪一个slave作为master呢？修改配置后，发现原来的master恢复了怎么办？")]),t._v(" "),a("p",[t._v("哨兵（sentinel）是一个分布式系统，用于对主从结构中每个节点进行监控，当出现故障时通过投票机制选择新的master并将所有的slave连接到master\n主要有三个功能：")]),t._v(" "),a("ul",[a("li",[t._v("监控： 不断检测maste和salve是否正常运作，master存货检测、master与slave运行情况检测等")]),t._v(" "),a("li",[t._v("通知： 当被监控的节点出现问题时，向其他哨兵发送通知")]),t._v(" "),a("li",[t._v("自动故障转移： 断开master与slave连接，选取一个slave作为master，将其他 slave连接到新的master，并告知客户端新的服务器地址")])]),t._v(" "),a("div",{staticClass:"custom-block warning"},[a("p",{staticClass:"custom-block-title"},[t._v("注意")]),t._v(" "),a("p",[t._v("哨兵也是服务，也要独立于redis启动，同时通常将哨兵数量设置为单数，以免发送选举平票的情况")])]),t._v(" "),a("h2",{attrs:{id:"创建setinel"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#创建setinel"}},[t._v("#")]),t._v(" 创建setinel")]),t._v(" "),a("p",[t._v("首先配置文件如下：")]),t._v(" "),a("div",{staticClass:"language-shell extra-class"},[a("pre",{pre:!0,attrs:{class:"language-shell"}},[a("code",[t._v("port "),a("span",{pre:!0,attrs:{class:"token number"}},[t._v("26379")]),t._v("\nsentinel monitor mymaster "),a("span",{pre:!0,attrs:{class:"token number"}},[t._v("192.168")]),t._v(".83.16 "),a("span",{pre:!0,attrs:{class:"token number"}},[t._v("6379")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token number"}},[t._v("2")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token comment"}},[t._v("#mymaster为自定义的sentinel名，后面的ip和端口为master，2表示有多少个哨兵认定挂了就确定为挂了")]),t._v("\n"),a("span",{pre:!0,attrs:{class:"token comment"}},[t._v("#sentinel auth-pass mymaster admin #sentinel 密码")]),t._v("\nsentinel down-after-milliseconds mymaster "),a("span",{pre:!0,attrs:{class:"token number"}},[t._v("15000")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token comment"}},[t._v("# 设置多少毫秒内无响应认定节点挂了")]),t._v("\nsentinel parallel-syncs mymaster "),a("span",{pre:!0,attrs:{class:"token number"}},[t._v("1")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token comment"}},[t._v("#使用多少个cpu去执行重新选举后执行的数据同步工作")]),t._v("\nsentinel failover-timeout mymaster "),a("span",{pre:!0,attrs:{class:"token number"}},[t._v("80000")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token comment"}},[t._v("#数据同步在多少秒内未完成表示失败")]),t._v("\n"),a("span",{pre:!0,attrs:{class:"token builtin class-name"}},[t._v("bind")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token number"}},[t._v("0.0")]),t._v(".0.0\n")])])]),a("p",[t._v("建立sentinel.yml的docker-compose 文件")]),t._v(" "),a("div",{staticClass:"language-yml extra-class"},[a("pre",{pre:!0,attrs:{class:"language-yml"}},[a("code",[a("span",{pre:!0,attrs:{class:"token key atrule"}},[t._v("version")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v(" "),a("span",{pre:!0,attrs:{class:"token string"}},[t._v("'3'")]),t._v("\n"),a("span",{pre:!0,attrs:{class:"token key atrule"}},[t._v("services")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v("\n  "),a("span",{pre:!0,attrs:{class:"token key atrule"}},[t._v("s26379")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v("\n    "),a("span",{pre:!0,attrs:{class:"token key atrule"}},[t._v("image")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v(" redis"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v("latest\n    "),a("span",{pre:!0,attrs:{class:"token key atrule"}},[t._v("restart")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v(" always\n    "),a("span",{pre:!0,attrs:{class:"token key atrule"}},[t._v("container_name")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v(" s26379\n    "),a("span",{pre:!0,attrs:{class:"token key atrule"}},[t._v("ports")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v("\n      "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("-")]),t._v(" 26379"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),a("span",{pre:!0,attrs:{class:"token number"}},[t._v("26379")]),t._v("\n    "),a("span",{pre:!0,attrs:{class:"token key atrule"}},[t._v("volumes")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v("\n      "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("-")]),t._v(" /usr/local/setinel/sentinel.conf"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v("/etc/redis/sentinel.conf\n    "),a("span",{pre:!0,attrs:{class:"token key atrule"}},[t._v("command")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v("\n      redis"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("-")]),t._v("sentinel /etc/redis/sentinel.conf\n  "),a("span",{pre:!0,attrs:{class:"token key atrule"}},[t._v("s26380")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v("\n    "),a("span",{pre:!0,attrs:{class:"token key atrule"}},[t._v("image")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v(" redis"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v("latest\n    "),a("span",{pre:!0,attrs:{class:"token key atrule"}},[t._v("restart")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v(" always\n    "),a("span",{pre:!0,attrs:{class:"token key atrule"}},[t._v("container_name")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v(" s26380\n    "),a("span",{pre:!0,attrs:{class:"token key atrule"}},[t._v("ports")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v("\n      "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("-")]),t._v(" 26380"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),a("span",{pre:!0,attrs:{class:"token number"}},[t._v("26379")]),t._v("\n    "),a("span",{pre:!0,attrs:{class:"token key atrule"}},[t._v("volumes")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v("\n      "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("-")]),t._v(" /usr/local/sentinel/sentinel.conf"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v("/etc/redis/sentinel.conf\n    "),a("span",{pre:!0,attrs:{class:"token key atrule"}},[t._v("command")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v("\n      redis"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("-")]),t._v("sentinel /etc/redis/sentinel.conf\n  "),a("span",{pre:!0,attrs:{class:"token key atrule"}},[t._v("s26381")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v("\n    "),a("span",{pre:!0,attrs:{class:"token key atrule"}},[t._v("image")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v(" redis"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v("latest\n    "),a("span",{pre:!0,attrs:{class:"token key atrule"}},[t._v("restart")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v(" always\n    "),a("span",{pre:!0,attrs:{class:"token key atrule"}},[t._v("container_name")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v(" s26381\n    "),a("span",{pre:!0,attrs:{class:"token key atrule"}},[t._v("ports")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v("\n     "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("-")]),t._v(" 26381"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),a("span",{pre:!0,attrs:{class:"token number"}},[t._v("26379")]),t._v("\n    "),a("span",{pre:!0,attrs:{class:"token key atrule"}},[t._v("volumes")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v("\n      "),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("-")]),t._v(" /usr/local/sentinel/setinel.conf"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v("/etc/redis/sentinel.conf\n    "),a("span",{pre:!0,attrs:{class:"token key atrule"}},[t._v("command")]),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v("\n      redis"),a("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("-")]),t._v("sentinel /etc/redis/sentinel.conf\n")])])]),a("p",[t._v("哨兵启动完毕后，将会重写sentinel.conf文件，配置sentinel集群的信息等")]),t._v(" "),a("h2",{attrs:{id:"工作原理"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#工作原理"}},[t._v("#")]),t._v(" 工作原理")])])}),[],!1,null,null,null);s.default=e.exports}}]);