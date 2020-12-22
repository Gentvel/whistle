package com.whistle.code.design.observer.eventreactor;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public interface Publisher {
    void add(Subscriber subscriber);
    void remove(Subscriber subscriber);
    void publish();

    void addEvent(Event<?> event);
    void removeEvent(Event<?> event);
}
