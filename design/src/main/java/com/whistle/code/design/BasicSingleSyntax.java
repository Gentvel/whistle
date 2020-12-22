package com.whistle.code.design;

import com.whistle.code.design.single.Hungry;
import com.whistle.code.design.single.Lazy;

import java.util.concurrent.CyclicBarrier;
import java.util.function.Consumer;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class BasicSingleSyntax {
    public static void main(String[] args) {
        //lazy
        Object instance = Lazy.getInstance();
        Object instance1 = Lazy.getInstance();
        System.out.println(instance == instance1);
        //Hungry
        Object instance2 = Hungry.getInstance();
        Object instance3 = Hungry.getInstance();
        System.out.println(instance3 == instance2);
    }
}
