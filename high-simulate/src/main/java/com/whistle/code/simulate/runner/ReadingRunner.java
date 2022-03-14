package com.whistle.code.simulate.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Lin
 * @version 1.0.0
 */
@Slf4j
@Component
public class ReadingRunner implements CommandLineRunner {
    @Resource
    private FileReading fileReading;
    @Override
    public void run(String... args) throws Exception {
        log.info("Running");
    }
}
