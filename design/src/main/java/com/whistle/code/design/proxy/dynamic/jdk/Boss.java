package com.whistle.code.design.proxy.dynamic.jdk;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class Boss implements Employee{
    @Override
    public void enter() {
        LocalTime enter = LocalTime.of(10, 30);
        System.out.println("Boss enter builder in "+ enter);
    }

    @Override
    public void work() {
        LocalTime of = LocalTime.of(2, 0);
        System.out.println("Boss work "+of);
    }

    @Override
    public void out() {
        LocalTime of = LocalTime.of(5, 30);
        System.out.println("Boss out "+ of);
    }
}
