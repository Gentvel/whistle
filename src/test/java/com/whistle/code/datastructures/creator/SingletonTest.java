package com.whistle.code.datastructures.creator;

import com.whistle.code.CommonTest;
import com.whistle.code.designpattern.creator.Game;
import com.whistle.code.designpattern.creator.singleton.Singleton;
import org.junit.Test;

/**
 * Description: <br>
 *
 * @auther: Gentvel
 * @since: 1.0
 * @see: com.whistle.code.datastructures.creator.SingletonTest
 */
public class SingletonTest extends CommonTest {
    @Test
    public void testLazySingleton(){
        Game singleton = Singleton.getLazyInstance();
        System.out.println(singleton);
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                Game instance = Singleton.getLazyInstance();
                System.out.println(instance);
            }).start();
        }
    }

    @Test
    public void testHungrySingleton(){
        Game hungryInstance = Singleton.getHungryInstance();
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                Game hungryInstance1 = Singleton.getHungryInstance();
                System.out.println(hungryInstance == hungryInstance1);
            }).start();
        }
    }
    @Test
    public void testDoubleCheckInstance(){
        Game instance  = Singleton.getDoubleCheckInstance();
        for (int i = 0; i < 10; i++) {
            new Thread(()->{

                Game checkInstance = Singleton.getDoubleCheckInstance();
                System.out.println(checkInstance == instance);
            }).start();

        }
    }


    @Test
    public void testInnerClassInstance(){
        Game instance = Singleton.getInnerInstance();
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                Game innerInstance = Singleton.getInnerInstance();
                System.out.println(instance == innerInstance);
            }).start();
        }
    }


    @Test
    public void testEnumInstance(){
        Game instance = Singleton.SingletonEnum.XYD.getInstance();
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                Game instance1 = Singleton.SingletonEnum.XYD.getInstance();
                System.out.println(instance==instance1);
            }).start();
        }
    }
}
