package com.whistle.code.netty.reactor.single;

import com.whistle.code.netty.reactor.Acceptor;
import com.whistle.code.netty.reactor.Handler;

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
public class Reactor {

    private Selector selector;


    private ServerSocketChannel serverSocketChannel;

    private Acceptor acceptor;

    private Handler handler;

    public Reactor(int port) {

        try {
            selector = Selector.open();

            serverSocketChannel = ServerSocketChannel.open();

            serverSocketChannel.socket().bind(new InetSocketAddress(port));

            serverSocketChannel.configureBlocking(false);

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            acceptor = new SingleAcceptor();

            handler = new SingleHandler();
        } catch (IOException e) {
            System.err.println("服务创建失败！");
            e.printStackTrace();
        }
    }

    public void select(){
        try {
            while (true){
                int select = selector.select();
                if(select>0){
                    dispatch();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void dispatch(){
        Set<SelectionKey> selectionKeys = selector.selectedKeys();



        Iterator<SelectionKey> iterator = selectionKeys.iterator();

        while (iterator.hasNext()){
            SelectionKey selectionKey = iterator.next();
            if(selectionKey.isAcceptable()){
                if (acceptor.accept(serverSocketChannel,selector)) {
                    System.out.println("客户端连接成功！");
                }
            }

            if(selectionKey.isConnectable()){
                System.out.println("客户端连接成功！2");
            }

            if(selectionKey.isReadable()){


                handler.read(selectionKey);
            }

            iterator.remove();
        }
    }




}
