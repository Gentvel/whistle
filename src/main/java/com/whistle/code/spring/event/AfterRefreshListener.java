package com.whistle.code.spring.event;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Description: <br>
 *
 * @auther: Gentvel
 * @since: 1.0
 * @see: com.whistle.code.spring.event.AfterRefresh
 */
@Component
public class AfterRefreshListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("AfterRefresh......");
    }
}
