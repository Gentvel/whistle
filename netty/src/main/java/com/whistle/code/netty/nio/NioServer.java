package com.whistle.code.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 *public static final int OP_READ = 1 << 0
 *
 * 值为1，表示读操作，
 * 代表本Channel已经接受到其他客户端传过来的消息，需要将Channel中的数据读取到Buffer中去
 *
 * public static final int OP_WRITE = 1 << 2
 *
 * 值为4，表示写操作
 * 一般临时将Channel的事件修改为它，在处理完后又修改回去。我暂时也没明白具体的作用。
 *
 * public static final int OP_CONNECT = 1 << 3
 *
 * 值为8，代表建立连接。
 * 一般在ServerSocketChannel上绑定该事件，结合 channel.finishConnect()在连接建立异常时进行异常处理
 *
 * public static final int OP_ACCEPT = 1 << 4
 *
 * 值为16，表示由新的网络连接可以accept。
 * 与ServerSocketChannel进行绑定，用于创建新的SocketChannel，并把其注册到Selector上去
 * @author Gentvel
 * @version 1.0.0
 */
public class NioServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(6888));
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        //将serverSocketChannel注册到socket，关心事件为OP_Accept
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //等待客户端连接
        while (true){
            if(selector.select(1000)==0){
                //没有事件发生
                System.out.println("无事件发生！");
                continue;
            }
            //如果select方法返回大于0 ，就能获取到有事件的selectionKeys集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                //OP_Accept事件
                if(selectionKey.isAcceptable()){
                    System.out.println("一个客户端连接了");
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    //将SocketChannel设置为非阻塞
                    socketChannel.configureBlocking(false);
                    //将当前客户端的channel注册到selector,关注事件为OP_READ,同时关联一个buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                //OP_READ事件
                if(selectionKey.isReadable()){
                    SocketChannel channel = (SocketChannel)selectionKey.channel();
                    ByteBuffer byteBuffer = (ByteBuffer)selectionKey.attachment();
                    channel.read(byteBuffer);
                    System.out.println("客户端："+ new String(byteBuffer.array()));
                }

                //


                //手动从集合中删除当前key，防止重复操作
                iterator.remove();
            }
        }


    }
}
