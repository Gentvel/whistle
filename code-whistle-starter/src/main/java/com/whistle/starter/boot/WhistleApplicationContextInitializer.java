package com.whistle.starter.boot;

import cn.hutool.core.bean.BeanDesc;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.system.SystemUtil;
import com.whistle.starter.WhistlePropertiesVerify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Gentvel
 */
@Slf4j
public class WhistleApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, EnvironmentPostProcessor {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> verify = WhistlePropertiesVerify.verify(environment);
        MapPropertySource propertiesPropertySource = new MapPropertySource("mutex-properties",verify);
        environment.getPropertySources().addFirst(propertiesPropertySource);
    }
}
