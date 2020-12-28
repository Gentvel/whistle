package com.whistle.code.springboot.extend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 *这个接口也只有一个方法：run(String... args)，触发时机为整个项目启动完毕后，自动执行。
 * 如果有多个CommandLineRunner，可以利用@Order来进行排序。
 * @author Gentvel
 * @version 1.0.0
 */
@Component
public class CustomCommandLineRunner implements CommandLineRunner {
    Logger logger = LoggerFactory.getLogger(CustomCommandLineRunner.class);

    @Override
    public void run(String... args) throws Exception {
        logger.info("用户扩展此接口，进行启动项目之后一些业务的预处理。");
    }
}
