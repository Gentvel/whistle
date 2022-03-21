package com.whistle.code.simulate.runner;

import com.whistle.code.simulate.entity.PersonInfo;
import com.whistle.code.simulate.service.PersonInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

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
        fileReading.read();
    }
}
