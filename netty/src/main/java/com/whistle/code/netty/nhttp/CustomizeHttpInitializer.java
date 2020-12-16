package com.whistle.code.netty.nhttp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class CustomizeHttpInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //向管道加入处理器

        //得到管道
        ChannelPipeline pipeline = ch.pipeline();
        //加入一个 netty 提供的 httpServerCodec：codec => [coder - decoder]
        //1、HttpServerCodec 是 netty 提供的处理http的编解码器
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        //2、增加自定义的Handler
        pipeline.addLast("CustomizeHttpHandler", new CustomizeHttpHandler());
    }
}
