package com.whistle.code.design.observer.eventreactor;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class Employee implements Subscriber{
    @Override
    public void subscribeEvent(Event<?> event) {
        Boolean o = (Boolean)event.get();
        if(o){
            System.out.println("服务上线了....员工很高兴");
        }else {
            System.out.println("服务下线了....员工不敢动");
        }
    }
}
