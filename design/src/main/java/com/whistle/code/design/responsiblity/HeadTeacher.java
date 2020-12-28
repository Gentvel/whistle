package com.whistle.code.design.responsiblity;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class HeadTeacher extends AbstractHandler<Integer> {


    @Override
    public void handleRequest(Integer days) {
        if(days>=2){
            if(getNext()!=null){
                getNext().handleRequest(days);
            }else{
                System.out.println("No one can approval");
            }
        }

        System.out.println("head teacher approval your application");
    }
}
