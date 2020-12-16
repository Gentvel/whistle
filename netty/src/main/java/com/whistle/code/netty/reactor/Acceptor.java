package com.whistle.code.netty.reactor;

import java.nio.channels.Channel;
import java.nio.channels.Selector;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
@FunctionalInterface
public interface Acceptor {
    /**
     * 注册channel
     * @param channel 注册channel
     * @param selector 注册selector
     * @return boolean
     */
    boolean accept(Channel channel, Selector selector);
}
