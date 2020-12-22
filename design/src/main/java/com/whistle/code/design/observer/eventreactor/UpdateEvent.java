package com.whistle.code.design.observer.eventreactor;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class UpdateEvent implements  Event<Boolean>{


    private boolean isUp;

    public UpdateEvent(boolean isUp){
        this.isUp = isUp;
    }

    @Override
    public Boolean get() {
        return isUp;
    }
}
