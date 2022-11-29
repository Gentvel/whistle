package com.whistle.basic;

import cn.hutool.extra.spring.SpringUtil;
import com.whistle.basic.redis.RedisTemplateDemo;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Objects;
/**
 * @author Zz
 */
@Component
public class BasicRunner implements CommandLineRunner, ApplicationContextAware {
    private ApplicationContext applicationContext;
    @Override
    public void run(String... args) {
        runTest(RedisTemplateDemo.class,"demo");
    }

    private void runTest(Class<?> clazz,String methodName){
        Object bean = applicationContext.getBean(clazz);
        Method method = ReflectionUtils.findMethod(clazz, methodName);
        if(Objects.isNull(method)){
            throw new RuntimeException(String.format("could not  find method name [%s]",methodName));
        }
        ReflectionUtils.makeAccessible(method);
        ReflectionUtils.invokeMethod(method,bean);
    }


    private void runTest(Class<?> clazz,String methodName,Object ...objects){
        Object bean = applicationContext.getBean(clazz);
        Method method = ReflectionUtils.findMethod(clazz, methodName);
        if(Objects.isNull(method)){
            throw new RuntimeException(String.format("could not  find method name [%s]",methodName));
        }
        ReflectionUtils.makeAccessible(method);
        ReflectionUtils.invokeMethod(method,bean,objects);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
