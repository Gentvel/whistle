package com.whistle.code.netty.reactor;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public interface Handler {
    /**
     * 读取内容
     * @param selectionKey key
     *
     */
    void read(SelectionKey selectionKey);
    /**
     * 发送内容
     */
    void send();


}
