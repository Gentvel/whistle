package com.whistle.code.design.single;

/**
 * 优点：这种写法比较简单，就是在类装载的时候就完成实例化。避免了线程同步问题。
 *
 * 缺点：在类装载的时候就完成实例化，没有达到Lazy Loading的效果。如果从始至终从未使用过这个实例，则会造成内存的浪费。
 * @author Gentvel
 * @version 1.0.0
 */
public class Hungry {
    private final static Object INSTANCE = new Object();

//    static{
//        INSTANCE = new Object();
//    }
    private Hungry(){

    }

    public static Object getInstance(){
        return INSTANCE;
    }
}
