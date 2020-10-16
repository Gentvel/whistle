package com.whistle.code.designpattern.constructor.proxy.dymicproxy.cglib;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Description: <br>
 *
 * @auther: Gentvel
 * @since: 1.0
 * @see: com.whistle.code.designpattern.constructor.proxy.dymicproxy.cglib.CglibProxy
 */
public class CglibProxy  implements MethodInterceptor {
    // 根据一个类型产生代理类，此方法不要求一定放在MethodInterceptor中
    public Object CreatProxyedObj(Class<?> clazz)
    {
        Enhancer enhancer = new Enhancer();

        enhancer.setSuperclass(clazz);

        enhancer.setCallback(this);

        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("Open GPA");
        Object invoke = methodProxy.invokeSuper(o, objects);
        System.out.println("Close GPA");
        return invoke;
    }
}
