package com.whistle.code.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class GroupChatHandler extends SimpleChannelInboundHandler<String> {

    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");


    private Map<String,Channel> channelMap = new ConcurrentHashMap<>();

    //定义channel组，管理所有channel
    //GlobalEventExecutor.INSTANCE 是全局事件执行器，是一个单例
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给其他客户
        channels.writeAndFlush(LocalDateTime.now().format(FORMATTER)+" [客户端] "+":"+channel.id()+"加入聊天");
        channels.add(channel);
    }
    //表示 channel 处于活动状态
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(LocalDateTime.now().format(FORMATTER)+" [客户端] "+":"+channel.id()+"上线了..");
    }
    //表示 channel 处于不活动状态
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(LocalDateTime.now().format(FORMATTER)+" [客户端] "+":"+channel.id()+"离线了..");
    }
    //表示 channel 断开连接
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.writeAndFlush(LocalDateTime.now().format(FORMATTER)+" [客户端] "+":"+channel.id()+"退出群聊");
        channels.remove(channel);
        System.out.println(LocalDateTime.now().format(FORMATTER)+" 当前群聊人数："+channels.size());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channels.forEach(ch->{
            if(ch!=channel){
                ch.writeAndFlush(LocalDateTime.now().format(FORMATTER)+" [客户端] "+":"+channel.id()+"发送消息:"+msg.trim());
            }else{
                ch.writeAndFlush(LocalDateTime.now().format(FORMATTER) + " 您发送了消息：" + msg.trim());
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //String message = cause.getMessage();
        //System.err.println(LocalDateTime.now().format(FORMATTER)+" "+ctx.channel().id()+" "+ message);
        ctx.close();
    }
}
