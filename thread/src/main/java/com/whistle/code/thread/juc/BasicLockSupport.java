package com.whistle.code.thread.juc;

import java.util.concurrent.locks.LockSupport;

/**
 *
 * LockSupport 提供 park()和 unpark()方法实现阻塞线程和解除线程阻塞。
 *
 * 阻塞线程：
 * void park()：阻塞当前线程，如果调用 unpark 方法或者当前线程被中断，才能从 park()方法中返回
 * void park(Object blocker)：功能同方法 1，入参增加一个 Object 对象，用来记录导致线程阻塞的阻塞对象，方便进行问题排查；
 * void parkNanos(long nanos)：阻塞当前线程，最长不超过 nanos 纳秒，增加了超时返回的特性；
 * void parkNanos(Object blocker, long nanos)：功能同方法 3，入参增加一个 Object 对象，用来记录导致线程阻塞的阻塞对象，方便进行问题排查；
 * void parkUntil(long deadline)：阻塞当前线程，直到 deadline；
 * void parkUntil(Object blocker, long deadline)：功能同方法 5，入参增加一个 Object 对象，用来记录导致线程阻塞的阻塞对象，方便进行问题排查；
 * 唤醒线程：
 * void unpark(Thread thread):唤醒处于阻塞状态的指定线程
 *
 * <strong>
 *     每个 park 方法都对应有一个带有 Object 阻塞对象的重载方法。增加了一个 Object 对象作为参数，此对象在线程受阻塞时被记录，以允许监视工具和诊断工具确定线程受阻塞的原因。
 * </strong>
 *
 *
 * 每个线程都会与一个许可关联，这个许可对应一个 Parker 的实例，Parker 有一个 int 类型的属性_count。
 *
 * park()方法：
 *
 * 将_count 变为 0
 * 如果原_count==0，将线程阻塞
 *
 * unpark()方法：
 *
 * 将_count 变为 1
 * 如果原_count==0，将线程唤醒
 *
 * <ul>
 *   <li>Object 的 wait()/notify 方法需要获取到对象锁之后在同步代码块里才能调用，而 LockSupport 不需要获取锁。所以使用 LockSupport 线程间不需要维护一个共享的同步对象，从而实现了线程间的解耦。</li>
 *   <li> unpark()方法可提前 park()方法调用，所以不需要担心线程间执行的先后顺序。</li>
 *   <li>多次调用 unpark()方法和调用一次 unpark()方法效果一样，因为 unpark 方法是直接将_counter 赋值为 1，而不是加 1。</li>
 *   <li>许可不可重入，也就是说只能调用一次 park()方法，如果多次调用 park()线程会一直阻塞。</li>
 * </ul>
 * @author Gentvel
 * @version 1.0.0
 */
public class BasicLockSupport {
    public static void main(String[] args) {
        Thread thread = new Thread(()->{
            LockSupport.park();
            System.out.println(Thread.currentThread().getName()+"已唤醒");
        });
        thread.start();

        LockSupport.unpark(thread);
    }
}
