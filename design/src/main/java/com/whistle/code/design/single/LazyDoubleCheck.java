package com.whistle.code.design.single;

import java.util.Objects;

/**
 * 线程安全；延迟加载；效率较高。
 * @author Gentvel
 * @version 1.0.0
 */
public class LazyDoubleCheck {
    private static Object instance;

    private LazyDoubleCheck(){

    }

    /**
     * 当两个线程同时执行，都处于判断完Object是否为空后，一个线程抢到了锁，另外一个等待，抢到锁的线程生成了instance然后释放锁，另一个线程获取锁
     * 发现此时instance不为空了，就直接返回
     * 对比
     * {@link LazySynchronize#getInstance}
     * {@link LazySynchronize#getInstanceFrom()}
     * @return
     */
    public static Object getInstance(){
        if(Objects.isNull(instance)){
            synchronized (LazyDoubleCheck.class){
                if(Objects.isNull(instance)){
                    instance = new Object();
                }
            }
        }
        return instance;
    }
}
