package com.whistle.code.netty.reactor.single;

import com.whistle.code.netty.reactor.Acceptor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
class SingleAcceptor implements Acceptor {

    private ServerSocketChannel serverSocketChannel;

    @Override
    public boolean accept(Channel channel, Selector selector) {
        if(channel instanceof ServerSocketChannel){
            serverSocketChannel = (ServerSocketChannel) channel;
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
            } catch (IOException e) {
                System.err.println("客户端连接错误！");
                e.printStackTrace();
            }
            return true;
        }

        return false;
    }
}
