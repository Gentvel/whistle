package com.whistle.code.springboot.extend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * 如果对一个方法标注了@PostConstruct，会先调用这个方法。
 * 这里重点是要关注下这个标准的触发点，这个触发点是在postProcessBeforeInitialization之后，InitializingBean.afterPropertiesSet之前。
 *
 * 用户可以对某一方法进行标注，来进行初始化某一个属性
 * @author Gentvel
 * @version 1.0.0
 */
@SpringBootApplication
public class SpringBootRunner {
    @Autowired
    private SchoolFactoryBean schoolFactoryBean;

    private static SchoolFactoryBean factoryBean;

    @PostConstruct
    private void  init(){
        factoryBean=schoolFactoryBean;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication springApplication = new SpringApplication(SpringBootRunner.class);
        springApplication.addInitializers(new CustomApplicationContextInitializer());
        //springApplication.setBannerMode(Banner.Mode.OFF);
        //springApplication.setLazyInitialization(true);
        springApplication.run(args);
        Student object = factoryBean.getObject();
        System.out.println(object.toString());
    }

}
