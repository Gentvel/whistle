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
}
