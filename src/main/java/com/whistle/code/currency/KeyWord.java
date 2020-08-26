package com.whistle.code.currency;

/**
 * Description: <br>
 *
 * @auther: Gentvel
 * @since: 1.0
 * @see: com.whistle.code.currency.KeyWord
 */
public class KeyWord {

    private volatile boolean flag = true;

    public void testVolatile(){
        int i = 0 ;
        new Thread(()->{
            while (flag){
                System.out.println("Running "+ i + " times");
            }
        }).start();
        new Thread(()->{
            flag = false;
            System.out.println("set flag = false");
        }).start();
    }
}
