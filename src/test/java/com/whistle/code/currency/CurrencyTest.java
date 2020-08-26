package com.whistle.code.currency;

import com.whistle.code.CommonTest;
import org.junit.Test;

/**
 * code - com.whistle.code.currency - CurrencyTest
 * Description: <br>
 *
 * @auther: Gentvel
 * @since: 1.0
 * @see: com.whistle.code.currency.CurrencyTest
 */
public class CurrencyTest extends CommonTest {
    @Test
    public void testThreadSleep() throws InterruptedException {
        OperateThread.threadSleep();
    }

    @Test
    public void testThreadJoin() throws InterruptedException {
        OperateThread.threadJoin();
        while (Thread.activeCount()>4){
            Thread.sleep(2000);
        }
    }
    @Test
    public void testThreadDaemon(){
        new OperateThread().testDaemon();
    }


    @Test
    public void testThreadYield(){
        new OperateThread().testYield();
    }

    @Test
    public void testThreadInterrupt(){
        Thread thread = new OperateThread().testInterrupt();
        Thread thread2 = new OperateThread().testInterrupt();
        thread.start();
        thread2.start();
        thread.interrupt();
        System.out.println(Thread.currentThread().getName()+"  thread is Interrupted  "+thread.isInterrupted());
        System.out.println(thread2.isInterrupted());
    }

    @Test
    public void testThreadVolatile(){
        new KeyWord().testVolatile();
    }
}
