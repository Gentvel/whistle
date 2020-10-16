package com.whistle.code.designpattern.constructor.proxy.dymicproxy.jdk;

import com.whistle.code.designpattern.constructor.proxy.Game;
import com.whistle.code.designpattern.constructor.proxy.LOL;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Description: <br>
 *
 * @auther: Gentvel
 * @since: 1.0
 * @see: com.whistle.code.designpattern.constructor.proxy.dymicproxy.jdk.GameHandler
 */
public class GameHandler implements InvocationHandler {
    Game game;
    public GameHandler(Game game){
        this.game = game;
    }

    /**
     * 代理逻辑
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Open GPA");

        Object invoke = method.invoke(game, args);

        System.out.println("Close GPA");
        return invoke;
    }
}
