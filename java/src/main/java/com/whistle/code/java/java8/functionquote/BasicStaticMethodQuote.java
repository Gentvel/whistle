package com.whistle.code.java.java8.functionquote;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class BasicStaticMethodQuote {

    public static int calculate(int x,int y){
        if(x==y) {
            return x+y;
        }
        if(x>y) {
            return x-y;
        }
        return x*y;
    }
}
