package com.whistle.code.netty.nhttp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class CustomizeHttpHandler extends SimpleChannelInboundHandler<HttpObject> {
    /**
     * 读取客户端数据。
     *
     * @param ctx
     * @param msg            客户端和服务器端互相通讯的数据被封装成 HttpObject
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if(msg instanceof HttpRequest){
            System.out.println("msg类型： "+msg.getClass());
            System.out.println("客户端地址： "+ ctx.channel().remoteAddress());
            HttpRequest httpRequest = (HttpRequest) msg;
            //获取uri，进行路径过滤
            URI uri = new URI(httpRequest.uri());
            if("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求了 favicon.ico,不做响应");
            }
            //回复给浏览器，http协议
            ByteBuf content = Unpooled.copiedBuffer("Hello , Client   ", StandardCharsets.UTF_8);

            DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            defaultFullHttpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            defaultFullHttpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            //将构建号的response返回
            ctx.writeAndFlush(defaultFullHttpResponse);

        }
    }
}
