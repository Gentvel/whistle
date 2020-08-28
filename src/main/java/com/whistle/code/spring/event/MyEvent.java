package com.whistle.code.spring.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ApplicationContextEvent;

/**
 * Description: <br>
 *
 * @auther: Gentvel
 * @since: 1.0
 * @see: com.whistle.code.spring.event.MyEvent
 */
public class MyEvent extends ApplicationEvent {
    /**
     * Create a new ContextStartedEvent.
     *
     * @param source the {@code ApplicationContext} that the event is raised for
     *               (must not be {@code null})
     */
    public MyEvent(ApplicationContext source) {
        super(source);
        System.out.println("My Event");
    }
}
