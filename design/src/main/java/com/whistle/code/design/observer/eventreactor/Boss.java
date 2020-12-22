package com.whistle.code.design.observer.eventreactor;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class Boss implements Subscriber{
    @Override
    public void subscribeEvent(Event<?> event) {
        if(!(Boolean) event.get()){
            System.out.println("服务下线了...老板很生气");
        }else{
            System.out.println("服务上线了....老板很高兴");
        }
    }
}
