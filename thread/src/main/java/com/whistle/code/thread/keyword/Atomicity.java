package com.whistle.code.thread.keyword;

/**
 * 由于count++是非原子性操作，它由三步组成：
 * <ul>
 *   <li>从主内存获取count拷贝</li>
 *   <li>操作count+1</li>
 *   <li>写回主内存</li>
 * </ul>
 * 当一个线程执行完获取拷贝时，挂起当前线程，其他线程操作+1，当前线程再次获取cpu时再执行++操作并写回主内存。
 * @author Gentvel
 * @version 1.0.0
 */
public class Atomicity {
    private volatile static int count = 0;


    private static void increment(){
        count++;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                for (int j = 0; j < 100; j++) {
                    increment();
                }
            },"Thread-"+i).start();
        }
        System.out.println(count);
    }
}
