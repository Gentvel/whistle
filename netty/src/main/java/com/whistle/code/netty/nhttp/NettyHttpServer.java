package com.whistle.code.netty.nhttp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class NettyHttpServer {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class).childHandler(new CustomizeHttpInitializer());


            ChannelFuture channelFuture = serverBootstrap.bind(8080).sync();
            channelFuture.addListener(cf->{
                if (cf.isSuccess()) {
                    System.out.println("服务端启动成功");
                }
                if (cf.isCancelled()) {
                    System.out.println("服务端启动失败");
                }
            });
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
