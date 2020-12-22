package com.whistle.code.design.proxy.dynamic.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class EmployeeInvocationHandler implements InvocationHandler {
    private Employee employee;

    public EmployeeInvocationHandler(Employee employee) {
        this.employee = employee;
    }
    /**
     * proxy:代表动态代理对象
     * method：代表正在执行的方法
     * args：代表调用目标方法时传入的实参
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(employee instanceof Boss){
            System.out.println("secretary said: ");
        }else{
            System.out.println("worker himself said :");
        }
        Object invoke = method.invoke(employee, args);
        return invoke;
    }
}
