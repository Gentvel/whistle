package com.whistle.code.design.proxy.dynamic.jdk;

import java.lang.reflect.Proxy;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class Client {
    public static void main(String[] args) {
        Boss boss = new Boss();
        EmployeeInvocationHandler employeeInvocationHandler = new EmployeeInvocationHandler(boss);
        Employee bossProxy = (Employee) Proxy.newProxyInstance(Employee.class.getClassLoader(), new Class<?>[]{Employee.class}, employeeInvocationHandler);
        bossProxy.enter();
        bossProxy.work();
        bossProxy.out();

        Worker worker = new Worker();
        EmployeeInvocationHandler employeeInvocationHandler1 = new EmployeeInvocationHandler(worker);
        Employee workerProxy = (Employee)Proxy.newProxyInstance(Employee.class.getClassLoader(), new Class<?>[]{Employee.class}, employeeInvocationHandler1);
        workerProxy.enter();
        workerProxy.work();
        workerProxy.out();
    }
}
