/**
 * Reactor模式，通过一个或多个输入同时传递给服务处理器的模式（基于事件驱动）
 * 服务器端程序处理传入的多个请求，并将它们同步分派到响应的处理线程，因此Reactor模式也叫Dispatcher模式。
 * Reactor模式使用IO复用监听事件，收到事件后，分发的某个线程（进程），这点就是网络服务高并发处理的关键。
 * Reactor模式中的核心组成部分：
 *
 * Reactor：Reactor在一个单独的线程中运行，负责监听和分发事件，分发给适当的处理程序来对IO事件作出反应。
 * 我的理解是将Reactor理解成一个Selector，它可以对建立新的连接，也可以将产生的读写事件交换给Handler进行处理
 * Handlers：处理程序执行I/O事件要完成的实际事件，类似于客户想要与之交谈的公司中的实际官员。Reactor通过调度适当的处理程序来响应I/O事件，处理程序执行非阻塞操作。
 *
 * @author Gentvel
 * @since 1.0
 */
package com.whistle.code.netty.reactor;