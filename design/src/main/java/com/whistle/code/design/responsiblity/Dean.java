package com.whistle.code.design.responsiblity;

/**
 * TODO
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class Dean extends AbstractHandler<Integer> {
    @Override
    public void handleRequest(Integer days) {
        if(days>=30){
            if(getNext()!=null){
                getNext().handleRequest(days);
            }else{
                System.out.println("No one can approval");
            }
        }

        System.out.println("Dean approval your application");
    }
}
