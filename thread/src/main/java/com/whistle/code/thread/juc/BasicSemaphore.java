package com.whistle.code.thread.juc;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Semaphore管理着一组许可permit，许可的初始数量通过构造函数设定。
 * <p>
 * 当线程要访问共享资源时，需要先通过acquire()方法获取许可。获取到之后许可就被当前线程占用了，在归还许可之前其他线程不能获取这个许可。
 * <p>
 * 调用acquire()方法时，如果没有许可可用了，就将线程阻塞，等待有许可被归还了再执行。
 * <p>
 * 当执行完业务功能后，需要通过release()方法将许可证归还，以便其他线程能够获得许可证继续执行。
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class BasicSemaphore {
    private static Semaphore semaphore = new Semaphore(3);

    public static void main(String[] args) {
        for (int i = 0; i < semaphore.getQueueLength() * 3; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();

                    System.out.println(Thread.currentThread().getName()+" enter parking lot");
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println(Thread.currentThread().getName()+" out of parking lot");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    semaphore.release();
                }

            }, "car" + i).start();
        }
    }

}
