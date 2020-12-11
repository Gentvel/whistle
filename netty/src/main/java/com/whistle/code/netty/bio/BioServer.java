package com.whistle.code.netty.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class BioServer {
    public static void main(String[] args) throws IOException {
        final ExecutorService executorService = Executors.newCachedThreadPool();
        //创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了");

        while (true) {
            //监听，等待客户端连接
            System.out.println("等待连接");
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端:"+socket.getRemoteSocketAddress());

            //创建一个线程，与之通讯
            executorService.execute(() -> {
                //重写Runnable方法，与客户端进行通讯
                handler(socket);
            });
        }
    }

    public static void handler(Socket socket) {
        //编写一个Handler方法，和客户端通讯
        try {
            byte[] bytes = new byte[1024];
            //通过socket获取输入流
            InputStream inputStream = socket.getInputStream();
            //循环的读取客户端发送的数据
            while (true){
                if (!socket.isClosed()){
                    System.out.println("线程信息：id= "+ Thread.currentThread().getId() + "; 线程名字：" + Thread.currentThread().getName());
                    int read = inputStream.read(bytes);
                    if (read != -1){
                        //输出客户端发送的数据
                        System.out.println(new String(bytes, 0, read));
                    } else {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭和client的连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
