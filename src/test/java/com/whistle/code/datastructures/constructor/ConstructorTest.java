package com.whistle.code.datastructures.constructor;

import com.whistle.code.CommonTest;
import com.whistle.code.designpattern.constructor.proxy.Game;
import com.whistle.code.designpattern.constructor.proxy.LOL;
import com.whistle.code.designpattern.constructor.proxy.TencentProxy;
import com.whistle.code.designpattern.constructor.proxy.dymicproxy.cglib.CglibProxy;
import com.whistle.code.designpattern.constructor.proxy.dymicproxy.jdk.GameHandler;
import org.junit.Test;
import org.springframework.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;

/**
 * Description: <br>
 *
 * @auther: Gentvel
 * @since: 1.0
 * @see: com.whistle.code.datastructures.constructor.ConstructorTest
 */
public class ConstructorTest extends CommonTest {
    @Test
    public void testProxy(){
        new TencentProxy().play();
    }
    @Test
    public void testDynamicProxy(){
        Game game = new LOL();
        GameHandler gameHandler = new GameHandler(game);
        Game o = (Game)Proxy.newProxyInstance(Game.class.getClassLoader(), new Class[]{Game.class}, gameHandler);
        o.play();
        System.out.println(o.getClass());

        Enhancer enhancer = new Enhancer();

        enhancer.setSuperclass(LOL.class);

        enhancer.setCallback(new CglibProxy());

        Game o1 = (Game) enhancer.create();

        o1.play();
    }
}
