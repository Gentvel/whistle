package com.whistle.code.netty.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Gentvel
 * @version 1.0.0
 */
public class Client {
    private volatile SocketChannel socketChannel;
    private Selector selector;

    private Thread messageThread;

    public Client() {
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open(new InetSocketAddress(6888));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ,ByteBuffer.allocate(1024));
        } catch (IOException e) {
            e.printStackTrace();
        }
        messageThread = new Thread(()->{
            if(socketChannel.isOpen()){
                readMessage();
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"Read-Message");
        messageThread.start();
    }


    public void sendMessage() throws IOException {
        Scanner scanner = new Scanner(System.in);
        String string;
        while (scanner.hasNextLine()){
            string= scanner.nextLine();
            socketChannel.write(ByteBuffer.wrap(string.getBytes()));
        }

        socketChannel.close();
        selector.close();
    }


    private void readMessage() {
        try {
            while (true) {
                TimeUnit.SECONDS.sleep(2);
                if (selector.select()>0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        if (selectionKey.isReadable()) {
                            SocketChannel channel = (SocketChannel) selectionKey.channel();
                            ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                            channel.read(byteBuffer);
                            System.out.println(new String(byteBuffer.array()).trim());
                        }
                        iterator.remove();
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }


}
