package com.whistle.code;

import org.junit.After;
import org.junit.Before;

/**
 * code - com.whistle.code - CommonTest
 * Description: <br>
 *
 * @auther: Gentvel
 * @since: 1.0
 * @see: com.whistle.code.CommonTest
 */
public abstract class CommonTest {
    private long currentTime;

    @Before
    public void before(){
        currentTime = System.currentTimeMillis();
    }

    @After
    public void after(){
        System.out.println("Running time: "+ (System.currentTimeMillis() - currentTime)+ " ms.");
    }
}
