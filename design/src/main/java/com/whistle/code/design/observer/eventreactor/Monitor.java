package com.whistle.code.design.observer.eventreactor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class Monitor implements Publisher{

    private List<Subscriber> list = new ArrayList<>();

    private Queue<Event<?>> queue =new ConcurrentLinkedQueue<>();

    @Override
    public void add(Subscriber subscriber) {
        Objects.requireNonNull(subscriber,"订阅者不能为空");
        list.add(subscriber);
    }

    @Override
    public void remove(Subscriber subscriber) {
        Objects.requireNonNull(subscriber,"订阅者不能为空");
        list.remove(subscriber);
    }



    @Override
    public void publish() {
        queue.stream().forEach(event1 -> list.forEach(subscriber -> subscriber.subscribeEvent(event1)));
    }

    @Override
    public void addEvent(Event<?> event) {
        Objects.requireNonNull(event,"事件不能为空");
        queue.add(event);
    }

    @Override
    public void removeEvent(Event<?> event) {
        Objects.requireNonNull(event,"事件不能为空");
        queue.remove(event);
    }

}
