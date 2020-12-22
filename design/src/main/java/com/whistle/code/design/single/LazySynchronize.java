package com.whistle.code.design.single;

import java.util.Objects;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class LazySynchronize {
    private static Object instance;

    private LazySynchronize(){
    }

    /**
     * 静态方法
     * 缺点：效率太低了，每个线程在想获得类的实例时候，执行getInstance()方法都要进行同步。
     * 而其实这个方法只执行一次实例化代码就够了，后面的想获得该类实例，直接return就行了。方法进行同步效率太低要改进。
     * @return Object
      */
    public static synchronized Object getInstance(){
        if(Objects.isNull(instance)){
            instance = new Object();
        }
        return instance;
    }

    /**
     * 静态代码块
     * 假如一个线程进入了if (singleton == null)判断语句块，还未来得及往下执行，另一个线程也通过了这个判断语句，这时便会产生多个实例。
     * @return instance
     */
    public static Object getInstanceFrom(){
        if(Objects.isNull(instance)){
            synchronized (LazySynchronize.class){
                instance = new Object();
            }
        }
        return instance;
    }
}
