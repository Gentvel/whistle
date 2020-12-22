package com.whistle.code.netty.groupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Gentvel
 * @version 1.0.0
 */
public class GroupChatServer {
    private int port;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    private ServerBootstrap serverBootstrap = null;

    private NioEventLoopGroup leader = null;
    private NioEventLoopGroup worker = null;

    public GroupChatServer(int port) {
        this.port = port;
    }

    public void init() {
        leader = new NioEventLoopGroup(1);
        worker = new NioEventLoopGroup();

        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(leader, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline()
                                .addLast("decoder", new StringDecoder())
                                .addLast("encoder", new StringEncoder())
                                .addLast("chat", new GroupChatHandler());
                    }
                });
        System.out.println(LocalDateTime.now().format(dateTimeFormatter)+" 服务器已初始化完成");
    }


    public void start() {
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.addListener(cf -> {
                if (cf.isSuccess()) {
                    System.out.println(LocalDateTime.now().format(dateTimeFormatter)+" 服务器启动成功...");
                }
                if (cf.isDone()) {
                    System.out.println(LocalDateTime.now().format(dateTimeFormatter)+" 服务器已就绪...");
                }
            });
            ChannelFuture close = channelFuture.channel().closeFuture().sync();
            close.addListener(cf -> {
                if (cf.isSuccess()) {
                    System.out.println(LocalDateTime.now().format(dateTimeFormatter)+" 服务端已关闭");
                }
            });
        } catch (InterruptedException e) {
            System.err.println(LocalDateTime.now().format(dateTimeFormatter)+" 服务器启动失败！");
            e.printStackTrace();
        } finally {
            leader.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer(6888);
        groupChatServer.init();
        groupChatServer.start();
    }
}
