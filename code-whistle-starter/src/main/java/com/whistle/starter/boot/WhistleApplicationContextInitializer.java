package com.whistle.starter.boot;

import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;
import com.whistle.starter.WhistlePropertiesVerify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author Gentvel
 */
@Slf4j
public class WhistleApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, EnvironmentPostProcessor, WebMvcConfigurer {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> verify = WhistlePropertiesVerify.verify(environment);
        MapPropertySource propertiesPropertySource = new MapPropertySource("mutex-properties",verify);
        environment.getPropertySources().addFirst(propertiesPropertySource);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        // 自定义配置...
        FastJsonConfig config = new FastJsonConfig();
        config.setCharset(StandardCharsets.UTF_8);
        // config.set...
        converter.setFastJsonConfig(config);
        // spring boot高版本无需配置，低版本不配置报错：Content-Type cannot contain wildcard type '*'
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        converter.setSupportedMediaTypes(fastMediaTypes);
        converters.add(0,converter);
    }

}
