package com.whistle.code.foundation.annotation;

/**
 * Description: <br>
 *
 * @auther: Gentvel
 * @since: 1.0
 * @see: com.whistle.code.foundation.annotation.PlayGame
 */
@Play({@Game("LOL"),@Game("XYD")})
public class PlayGame {
    public void play(){
        System.out.println("I am play game");
    }
}
