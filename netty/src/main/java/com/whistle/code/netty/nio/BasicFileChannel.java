package com.whistle.code.netty.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Gentvel
 * @version 1.0.0
 */
public class BasicFileChannel {
    public static void main(String[] args) throws IOException {
        String str = "Hello FileChannel!";
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\channel.txt");
        final FileChannel channel = fileOutputStream.getChannel();
        final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(str.getBytes());
        byteBuffer.flip();
        channel.write(byteBuffer);
        channel.close();
        fileOutputStream.close();

        final FileInputStream fileInputStream = new FileInputStream("D:\\channel.txt");
        final FileChannel channel1 = fileInputStream.getChannel();
        byteBuffer.clear();
        channel1.read(byteBuffer);
        byteBuffer.flip();
        System.out.println(new String(byteBuffer.array()));
        channel1.close();
        fileInputStream.close();
    }
}