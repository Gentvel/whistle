package com.whistle.code.netty.chat;

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
public class Server {
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    public Server() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            selector = Selector.open();

            serverSocketChannel.socket().bind(new InetSocketAddress(6888));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            System.err.println("服务端启动失败！");
            e.printStackTrace();
        }
        System.out.println("服务端启动成功！");

    }

    public void listen() {
        try {
            while (true) {
                int select = selector.select();
                if (select > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        if (selectionKey.isAcceptable()) {
                            acceptClient();
                        }

                        if (selectionKey.isReadable()) {
                            readClient(selectionKey);
                        }
                        iterator.remove();
                    }
                } else {
                    System.out.println("等待客户端连接...");
                }
            }
        } catch (IOException e) {
            System.err.println("客户端连接发生错误！");
            e.printStackTrace();
        }
    }

    private void acceptClient() {
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            System.out.println("一个客户端上线了：" + socketChannel.hashCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readClient(SelectionKey selectionKey) {
        SocketChannel channel = null;
        try{
            channel=(SocketChannel) selectionKey.channel();
            ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
            int read = channel.read(byteBuffer);
            //当read=-1时，说明客户端的数据发送完毕，并且主动的关闭socket。所以这种情况下，服务器程序需要关闭socketSocket，并且取消key的注册。注意：这个时候继续使用SocketChannel进行读操作的话，就会抛出：==远程主机强迫关闭一个现有的连接==的IO异常
            if(read>0){
                String msg = new String(byteBuffer.array());
                System.out.println("Chanel:"+channel.hashCode()+"发来消息："+msg);

                distribution(channel,msg);
            }
        }catch (Exception e){
            try {
                System.out.println(channel.getRemoteAddress() + "离线了...");
                //取消注册
                selectionKey.cancel();
                //关闭通道
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void distribution(SocketChannel channel, String msg) throws IOException {

        System.out.println("服务器消息转发中....");
        for (SelectionKey key : selector.keys()) {
            Channel target = key.channel();
            if(target!=channel&&target instanceof SocketChannel){
                SocketChannel socketChannel = (SocketChannel)target;
                socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
            }
        }
        System.out.println("消息转发完毕!");
    }
}
