package com.whistle.code.netty.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * MappedByteBuffer 可以让文件直接在内存（堆外内存）修改，操作系统不需要拷贝一次
 * @author Gentvel
 * @version 1.0.0
 */
public class BasicMappedByteBuffer {
    public static void main(String[] args) throws IOException {
        RandomAccessFile rw = new RandomAccessFile("D:\\channel.txt", "rw");
        FileChannel channel = rw.getChannel();
        /**
         * 参数1：使用的模式读写
         * 参数2：可以直接修改的起始位置
         * 参数3：映射到内存的大小，即将文件的多少个字节映射到内存中
         */
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        map.put(0,(byte)'H');
        map.put(2,(byte)'H');
        map.put(3,(byte)'H');

        channel.close();
        rw.close();

    }
}
