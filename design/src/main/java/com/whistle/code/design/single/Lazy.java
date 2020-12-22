package com.whistle.code.design.single;

import java.io.Serializable;
import java.util.Objects;

/**
 * 懒汉式
 * 如果在多线程下，一个线程进入了if (singleton == null)判断语句块，还未来得及往下执行，
 * 另一个线程也通过了这个判断语句，这时便会产生多个实例。所以在多线程环境下不可使用这种方式。
 * @author Gentvel
 * @version 1.0.0
 */
public class Lazy implements Serializable {
    private static Object object;

    private Lazy(){
        if (object != null) {
            throw new RuntimeException("could not instance singleton object");
        }
    }
    public static Object getInstance(){
        if(Objects.isNull(object)){
            object = new Object();
        }
        return object;
    }

    /**
     * 在反序列化时，直接调用这个方法，返回指定的对象，无需再新建一个对象
     * @return
     */
    private Object readResolve() {
        return object;
    }
}
