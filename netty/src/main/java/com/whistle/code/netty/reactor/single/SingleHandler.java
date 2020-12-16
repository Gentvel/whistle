package com.whistle.code.netty.reactor.single;

import com.whistle.code.netty.reactor.Handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.StringJoiner;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class SingleHandler implements Handler {


    @Override
    public void read(SelectionKey selectionKey) {

        ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();

        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();


        try {
            StringJoiner stringJoiner = new StringJoiner(",","{","}");
            if (socketChannel.read(byteBuffer)>0) {
                stringJoiner.add(new String(byteBuffer.array()).trim());
                byteBuffer.clear();
            }

            if(2==stringJoiner.length()){
                throw new IOException();
            }else{
                System.out.println("接收到消息："+ stringJoiner+" 长度： "+stringJoiner.length());
            }
            //开始响应
            String string = "OK，I get it";
            byteBuffer.put(string.getBytes());
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            System.out.println("响应消息："+string);
        } catch (IOException e) {
            System.err.println("客户端连接已关闭！");
            selectionKey.cancel();
            try {
                socketChannel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @Override
    public void send() {

    }
}
