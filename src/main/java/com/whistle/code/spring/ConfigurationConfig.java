package com.whistle.code.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

/**
 * Description: <br>
 *
 * @auther: Gentvel
 * @since: 1.0
 * @see: com.whistle.code.spring.Configuration
 */
@Configuration
@ComponentScan
@Import(ConfigurationConfig.class)
@PropertySource("classpath:org/application.yml")
public class ConfigurationConfig {
}
