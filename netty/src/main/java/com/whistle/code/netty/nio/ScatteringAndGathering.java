package com.whistle.code.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering 将数据写入到buffer时，可以采用buffer数组，依次写入【分散】
 * Gathering 从buffer读取数据时，可以采用buffer数组，依次读取【聚合】
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class ScatteringAndGathering {
    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(6888);
        serverSocketChannel.socket().bind(inetSocketAddress);

        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(7);

        SocketChannel socketChannel = serverSocketChannel.accept();


        int messageLength = 12;

        while (true) {
            int bytesRead = 0;

            while (bytesRead < messageLength) {
                long read = socketChannel.read(byteBuffers);
                bytesRead += read;
                Arrays.stream(byteBuffers).map(byteBuffer -> "position=" + byteBuffer.position() +
                        ",limit = " + byteBuffer.limit()).forEach(System.out::println);
            }
            //读数据后需要将所有的buffer进行flip
            Arrays.asList(byteBuffers).forEach(Buffer::flip);
            //将数据读出显示到客户端
            long byteWrite = 0;
            while (byteWrite < messageLength) {
                long l = socketChannel.write(byteBuffers);
                byteWrite += l;
            }

            //将所有的 buffer 进行clear操作
            Arrays.asList(byteBuffers).forEach(Buffer::clear);
            System.out.println("byteRead=" + bytesRead + ", byteWrite=" + byteWrite
                    + ", msgLength=" + messageLength);

        }

    }
}
