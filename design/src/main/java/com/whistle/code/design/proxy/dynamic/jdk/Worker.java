package com.whistle.code.design.proxy.dynamic.jdk;

import java.time.LocalTime;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class Worker implements Employee{
    @Override
    public void enter() {
        LocalTime of = LocalTime.of(9, 0);
        System.out.println("Worker enter build at "+ of);
    }

    @Override
    public void work() {
        System.out.println("Worker work "+ LocalTime.of(8,0));
    }

    @Override
    public void out() {
        System.out.println("worker out build at "+ LocalTime.of(6,30));
    }
}
