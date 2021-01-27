---
title: 【Netty】 NIO 非阻塞式IO
date: 2020-12-14
sidebar: auto
categories:
 - 中间件
tags:
- netty
prev: ./bio
next: ./architecture
---

## NIO简介
与S ocket 类和ServerSocket 类相对应， NIO 也提供了SocketChanneI 和ServerSocketChanneI两种不同的套接字通道实现。这两种新增的通道都支持阻塞和非阻塞两种模式。阻塞模式使用非常简单， 但是性能和可靠性都不好， 非阻塞模式则正好相反。开发人员一般可以根据自己的需要来选择合适的模式， 一般来说， 低负载、低并发的应用程序可以选择同步阻塞I/O 以降低编程复杂度， 但是对于高负载、高并发的网络应用， 需要使用NIO 的非阻塞模式进行开发。

## NIO类库简介
### Buffer
在NIO 库中， 所有数据都是用缓冲区处理的。在读取数据时， 它是直接读到缓冲区中的； 在写入数据时， 写入到缓冲区中。任何时候访问NIO 中的数据， 都是通过缓冲区进行操作。缓冲区实质上是一个数组。通常它是一个字节数组(ByteBuffer) ， 也可以使用其他种类的数组。但是一个缓冲区不仅仅是一个数组， 缓冲区提供了对数据的结构化访问以及维护读写位置(limit) 等信息。


最常用的缓冲区是ByteBuffer, 一个ByteBuffer 提供了一组功能用于操作byte 数组。除了ByteBuffer ， 还有其他的一些缓冲区， 事实上， 每一种Java 基本类型（ 除了Boolean
类型） 都对应有一种缓冲区， 具体如下:

- ByteBuffer 字节缓冲区
- CharBuffer 字符缓冲区
- ShortBuffer 短整型缓冲区
- LongBuffer 长整型缓冲区
- FloatBuffer 浮点型缓冲区
- Doublebuffer 双精度浮点型缓冲区

还有一种特殊的缓冲区 MappedByteBuffer 将在零拷贝篇章讲到。这些对应的缓冲区都继承于Buffer类，具体实现都在子类中有个hb的属性。

### Channel 

Channel 是一个通道， 可以通过它读取和写入数据， 它就像自来水管一样， 网络数据通过Channel 读取和写入。通道与流的不同之处在于通道是双向的， 流只是在一个方向上移动（ 一个流必须是InputStream 或者OutputStream 的子类） ， 而且通道可以用于读、写或者同时用于读写。

因为Channel 是全双工的， 所以它可以比流更好地映射底层操作系统的API 特别是在UNIX 网络编程模型中， 底层操作系统的通道都是全双工的， 同时支持读写操作。

### Selector(多路复用器)

多路复用器提供选择已经就绪的任务的能力。简单来讲， selector 会不断地轮询注册在其上的Channel ， 如果某个Channel 上面有新的TCP连接接入、读和写事件， 这个Channel 就处于就绪状态， 会被selector 轮询出来， 然后通过SelectionKey 可以获取就绪Channel 的集合， 进行后续的I/O 操作

一个多路复用器Selector 可以同时轮询多个Channel ， 由于JDK 使用了epoll() 代替传统的select 实现， 所以它并没有最大连接句柄1024 / 2048 的限制。这也就意味着只需要一个线程负责selector 的轮询， 就可以接入成千上万的客户端， 这确实是个非常巨大的进步。

## 简单代码示例
```java
package com.whistle.code.day.netty.day08;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Gentvel
 * @version 1.0.0
 */
public class NIOServer {
    public void initialize(Integer port) {
        try (Selector selector = Selector.open();
             ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true){
                if (selector.select()>0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()){
                        SelectionKey next = iterator.next();
                        if(next.isAcceptable()){
                            SocketChannel accept = serverSocketChannel.accept();

                            accept.configureBlocking(false);
                            accept.register(selector,SelectionKey.OP_READ);
                            System.out.println("一个客户端已连接:"+accept.getRemoteAddress());

                        }

                        if(next.isReadable()){
                            SocketChannel channel = (SocketChannel)next.channel();
                            ByteBuffer allocate = ByteBuffer.allocate(1024);
                            while (channel.read(allocate)!=-1){
                                allocate.flip();
                                System.out.println(new String(allocate.array()).trim());
                                allocate.rewind();
                            }
                        }
                        iterator.remove();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

总结来说，NIO的优点如下：  
（ 1 ） 客户端发起的连接操作是异步的， 可以通过在多路复用器注册op CONNECT 等待后续结果， 不需要像之前的客户端那样被同步阻塞。  
（ 2 ） SocketChanneI 的读写操作都是异步的， 如果没有可读写的数据它不会同步等待，直接返回， 这样I ／ 0 通信线程就可以处理其他的链路， 不需要同步等待这个链路可用。  
（ 3 ） 线程模型的优化： 山于JDK 的selector 在Linux 等主流操作系统上通过epoll 实现， 它没有连接句柄数的限制（ 只受限于操作系统的最大句柄数或者对单个进程的句柄限制） ， 这意味着一个selector 线程可以同时处理成千上万个客户端连接， 而且性能不会随着客户端的增加而线性下降， 因此， 它非常适合做高性能、高负载的网络服务器。


:::warning
还有一种叫AIO，也即是异步I/O，由AsynchronousSocketChannel实现，这里不再编写
:::





