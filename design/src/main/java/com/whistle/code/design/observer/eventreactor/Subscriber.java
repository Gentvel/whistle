package com.whistle.code.design.observer.eventreactor;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public interface Subscriber {
    void subscribeEvent(Event<?> event);
}
