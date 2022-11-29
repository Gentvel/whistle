package com.whistle.basic;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Zz
 */
@Slf4j
@Component
public class BasicRunner implements CommandLineRunner, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void run(String... args) {
        Map<String, DemoRunner> beansOfType = applicationContext.getBeansOfType(DemoRunner.class);

        beansOfType.forEach((name,bean)->{
            TimeInterval timer = DateUtil.timer();
            timer.start();
            bean.runner();
            long interval = timer.interval();
            log.info("exec bean runner [{}] ,exec time {} ms.",name,interval);
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
