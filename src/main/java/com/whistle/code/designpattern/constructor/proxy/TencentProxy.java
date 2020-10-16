package com.whistle.code.designpattern.constructor.proxy;

/**
 * Description: <br>
 *
 * @auther: Gentvel
 * @since: 1.0
 * @see: com.whistle.code.designpattern.constructor.proxy.TencentProxy
 */
public class TencentProxy implements Game{

    private Game txGame = new LOL();

    @Override
    public void play() {
        System.out.println("Welcome Open Game Assist");
        txGame.play();
        System.out.println("Close GPA");
    }
}
