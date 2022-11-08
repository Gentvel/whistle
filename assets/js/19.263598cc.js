(window.webpackJsonp=window.webpackJsonp||[]).push([[19],{526:function(t,a,_){t.exports=_.p+"assets/img/osRuntime.0496cd3f.jpg"},527:function(t,a,_){t.exports=_.p+"assets/img/break.66aa2c6e.png"},528:function(t,a,_){t.exports=_.p+"assets/img/breakType.cf9e25c8.png"},640:function(t,a,_){"use strict";_.r(a);var s=_(4),r=Object(s.a)({},(function(){var t=this,a=t.$createElement,s=t._self._c||a;return s("ContentSlotsDistributor",{attrs:{"slot-key":t.$parent.slotKey}},[s("h2",{attrs:{id:"一、os的运行机制和体系结构"}},[s("a",{staticClass:"header-anchor",attrs:{href:"#一、os的运行机制和体系结构"}},[t._v("#")]),t._v(" 一、OS的运行机制和体系结构")]),t._v(" "),s("p",[s("img",{attrs:{src:_(526),alt:"runtime"}})]),t._v(" "),s("h2",{attrs:{id:"二、运行机制"}},[s("a",{staticClass:"header-anchor",attrs:{href:"#二、运行机制"}},[t._v("#")]),t._v(" 二、运行机制")]),t._v(" "),s("h3",{attrs:{id:"_2-1-两种指令"}},[s("a",{staticClass:"header-anchor",attrs:{href:"#_2-1-两种指令"}},[t._v("#")]),t._v(" 2.1 两种指令")]),t._v(" "),s("ul",[s("li",[t._v("非特权指令"),s("br"),t._v("\n比如一些逻辑运算或者数学运算指令")]),t._v(" "),s("li",[t._v("特权指令"),s("br"),t._v("\n比如对内存的操作指令，如果用户程序可以使用这个指令，就意味着一个用户可以将其他用户的内存随意清除，这样显然是很危险的")])]),t._v(" "),s("h3",{attrs:{id:"_2-2-两种处理器状态"}},[s("a",{staticClass:"header-anchor",attrs:{href:"#_2-2-两种处理器状态"}},[t._v("#")]),t._v(" 2.2 两种处理器状态")]),t._v(" "),s("ul",[s("li",[t._v("用户态（目态）"),s("br"),t._v("\n此时CPU只能处理非特权指令")]),t._v(" "),s("li",[t._v("核心态（管态）"),s("br"),t._v("\n此时CPU能处理特权指令与非特权指令")])]),t._v(" "),s("div",{staticClass:"custom-block tip"},[s("p",[t._v("用程序状态寄存器的某标志位来保存当前处理器处于什么状态（如0为用户态，1为核心态）")])]),t._v(" "),s("h3",{attrs:{id:"_2-3-两种程序"}},[s("a",{staticClass:"header-anchor",attrs:{href:"#_2-3-两种程序"}},[t._v("#")]),t._v(" 2.3 两种程序")]),t._v(" "),s("ul",[s("li",[t._v("用户程序"),s("br"),t._v("\n为了保证系统的安全运行，普通应用程序只能执行非特权指令，运行在用户态")]),t._v(" "),s("li",[t._v("内核程序"),s("br"),t._v("\n操作系统的内核程序是系统的管理者，即可以执行特权指令也可以执行非特权指令，运行在用户态")])]),t._v(" "),s("h2",{attrs:{id:"三、内核"}},[s("a",{staticClass:"header-anchor",attrs:{href:"#三、内核"}},[t._v("#")]),t._v(" 三、内核")]),t._v(" "),s("p",[t._v("在操作系统中，并不是所有的软件都是核心的，比如windows上的记事本，linux上的yum，即使没有这些仍然可以使用计算机\n内核是计算机配置的底层软件，是操作系统最基本、最核心的部分。\n操作系统分为内核程序和非内核功能\n内核程序中包括时钟管理、中断处理、原语、资源管理")]),t._v(" "),s("h3",{attrs:{id:"_3-1-时钟管理"}},[s("a",{staticClass:"header-anchor",attrs:{href:"#_3-1-时钟管理"}},[t._v("#")]),t._v(" 3.1 时钟管理")]),t._v(" "),s("p",[t._v("实现计算机的计时功能，所有的进程切换和调度管理工作都需要计时管理")]),t._v(" "),s("h3",{attrs:{id:"_3-2-原语"}},[s("a",{staticClass:"header-anchor",attrs:{href:"#_3-2-原语"}},[t._v("#")]),t._v(" 3.2 原语")]),t._v(" "),s("p",[t._v("原语是一种特殊的程序，它负责设备驱动、CPU切换等工作，是最接近硬件的部分，这种程序的运行据有原子性")]),t._v(" "),s("h3",{attrs:{id:"_3-3-中断功能"}},[s("a",{staticClass:"header-anchor",attrs:{href:"#_3-3-中断功能"}},[t._v("#")]),t._v(" 3.3 中断功能")]),t._v(" "),s("center",[s("p",[s("img",{attrs:{src:_(527),alt:"runtime"}})])]),t._v(" "),s("h4",{attrs:{id:"中断机制的诞生"}},[s("a",{staticClass:"header-anchor",attrs:{href:"#中断机制的诞生"}},[t._v("#")]),t._v(" "),s("strong",[t._v("中断机制的诞生")])]),t._v(" "),s("p",[t._v("早期的计算机中，计算机只能串行的运行程序，当需要调用IO等操作时，cpu会等待IO操作完成后再进行运算移出程序，再运行其他程序。所以在计算机中同一时刻只有一个程序在运行，这样就会导致系统资源的利用率特别低。于是，人们发明了操作系统（作为计算机的管理者），引入中断机制，实现多道程序并发执行。"),s("strong",[t._v("只要发生中断操作就意味着需要操作系统介入，开展管理工作。")])]),t._v(" "),s("h4",{attrs:{id:"中断机制概念"}},[s("a",{staticClass:"header-anchor",attrs:{href:"#中断机制概念"}},[t._v("#")]),t._v(" "),s("strong",[t._v("中断机制概念")])]),t._v(" "),s("p",[t._v("当多个进程在计算机种运行时，运行在用户态中，当CPU收到计时部件发出的中断信号，切换为"),s("strong",[t._v("核心态")]),t._v("对中断进行处理。然后把CPU的使用权限交给操作系统，操作系统内核对中断信号进行处理。")]),t._v(" "),s("p",[t._v("如果操作系统发现中断信号是提示当前程序时间片已用完，那么就会切换别的进程运行。此时操作系统会将CPU权限交给用户进程，切换为用户态。")]),t._v(" "),s("p",[t._v("如果操作系统发现中断信号为发出系统调用（内中断信号），CPU切换为核心态，操作系统对中断进行处理，然后暂停当前进程，等待该进程请求资源完成，切换进程运行。")]),t._v(" "),s("p",[t._v("当之前进程请求资源完成后，该资源会向CPU发出中断信号。CPU切换为核心态让操作系统进行处理，确认之前进程资源请求完成，让其恢复运行，完成后续操作。")]),t._v(" "),s("ul",[s("li",[t._v("当中断发生时，CPU立即进入核心态")]),t._v(" "),s("li",[t._v("当中断发生后，当前运行的进程暂停运行，并由操作系统内核对中断进行处理")]),t._v(" "),s("li",[t._v("对于不同的中断信号，会进行不同的处理")])]),t._v(" "),s("div",{staticClass:"custom-block tip"},[s("p",[t._v("发生了中断就意味着需要操作系统介入，开展管理工作，由于操作系统的管理工作（比如进程切换、分配资源等）需要使用特权指令，因此CPU需要从用户态转为核心态。中断可以从用户态切换为核心态，让操作系统获得计算机的控制权。实现多道程序的并发执行。")])]),t._v(" "),s("h4",{attrs:{id:"中断的分类"}},[s("a",{staticClass:"header-anchor",attrs:{href:"#中断的分类"}},[t._v("#")]),t._v(" "),s("strong",[t._v("中断的分类")])]),t._v(" "),s("center",[s("p",[s("img",{attrs:{src:_(528),alt:"runtime"}})])]),t._v(" "),s("ul",[s("li",[t._v("内中断"),s("br"),t._v("\n信号来源：CPU内部，与当前执行的指令有关")]),t._v(" "),s("li",[t._v("外中断\n信号来源: CPU外部，与当前执行的指令无关")])]),t._v(" "),s("h3",{attrs:{id:"_3-4-资源管理"}},[s("a",{staticClass:"header-anchor",attrs:{href:"#_3-4-资源管理"}},[t._v("#")]),t._v(" 3.4 资源管理")]),t._v(" "),s("ul",[s("li",[t._v("进程管理")]),t._v(" "),s("li",[t._v("存储器管理")]),t._v(" "),s("li",[t._v("设备管理")])]),t._v(" "),s("h2",{attrs:{id:"四、体系结构"}},[s("a",{staticClass:"header-anchor",attrs:{href:"#四、体系结构"}},[t._v("#")]),t._v(" 四、体系结构")]),t._v(" "),s("h3",{attrs:{id:"_4-1-大内核"}},[s("a",{staticClass:"header-anchor",attrs:{href:"#_4-1-大内核"}},[t._v("#")]),t._v(" 4.1 大内核")]),t._v(" "),s("p",[t._v("即是包括内核程序中的资源管理称为大内核，运行在核心态。\n高性能，但是内核代码庞大、结构不好维护。")]),t._v(" "),s("h3",{attrs:{id:"_4-2-微内核"}},[s("a",{staticClass:"header-anchor",attrs:{href:"#_4-2-微内核"}},[t._v("#")]),t._v(" 4.2 微内核")]),t._v(" "),s("p",[t._v("只把内核程序最基本的功能保留在内核，即没有资源管理部分。\n这种优点是内核功能少、结构清晰、方便维护。但是需要频繁从核心态与用户态之间的转换，性能低")])],1)}),[],!1,null,null,null);a.default=r.exports}}]);