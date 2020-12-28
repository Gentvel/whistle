package com.whistle.code.design.responsiblity;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class HeadDepartment extends AbstractHandler<Integer>{
    @Override
    public void handleRequest(Integer days) {
        if(days>=7){
            if(getNext()!=null){
                getNext().handleRequest(days);
            }else{
                System.out.println("No one can approval");
            }
        }

        System.out.println("head department approval your application");
    }
}
