package com.whistle.code.easycode.test;

import com.whistle.code.easycode.annotation.EnableEasyCode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
@EnableEasyCode
@SpringBootApplication
public class SpringRunner {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SpringRunner.class, args);
    }

}
