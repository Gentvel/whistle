package com.whistle.code.netty.ntcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class NettyServer {
    public static void main(String[] args) {
        //bossGroup线程组，只处理连接请求，无线循环
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        //workerGroup线程组，处理真正业务，无限循环
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //创建服务端启动对象，配置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //设置线程组，
            serverBootstrap.group(bossGroup,workerGroup)
                    //使用NioServerSocketChannel作为服务通道实现
                    .channel(NioServerSocketChannel.class)
                    //设置线程队列得到连接个数
                    .option(ChannelOption.SO_BACKLOG,128)
                    //设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    //给 pipeline 添加处理器，每当有连接accept时，就会运行到此处。
                    .childHandler(new ChannelInitializer<>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });
            System.out.println("........服务器 is ready...");
            //绑定一个端口并且同步，生成了一个ChannelFuture 对象
            //启动服务器（并绑定端口）
            ChannelFuture future = serverBootstrap.bind(6888).sync();
            future.addListener(cf->{
                if (cf.isSuccess()) {
                    System.out.println("监听端口：6888成功");
                }
                if(cf.isDone()){
                    System.out.println("监听端口：已完成");
                }
                if (cf.isCancelled()) {
                    System.out.println("监听端口已取消");
                }
            });
            //对关闭通道进行监听
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
