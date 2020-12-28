package com.whistle.code.spring.customize;

import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * initPropertySources正符合Spring的开放式结构设计，给用户最大扩展Spring的能力。
 * 用户可以根据自身的需要重写initPropertySources方法，并在方法中进行个性化的属性处理及设置。
 * validateRequiredProperties则是对属性进行验证
 * @author Gentvel
 * @version 1.0.0
 */
public class CustomAnnotationConfigApplicationContext extends AnnotationConfigApplicationContext {
    public CustomAnnotationConfigApplicationContext(Class<?>... componentClasses) {
        super(componentClasses);
    }

    @Override
    public void refresh() throws BeansException, IllegalStateException {
        super.refresh();
    }

    /**
     * 假如现在有这样一个需求，工程在运行过程中用到的某个设置（例如VAR）是从系统环境变量中取得的，而如果用户没有在系统环境变量中配置这个参数，那么工程可能不会工作。
     * 这一要求可能会有各种各样的解决办法，当然，在Spring中可以这样做，你可以直接修改Spring的源码，例如拓展ApplicationContext。
     *
     * 当然，在springboot中更简单，直接使用EL表达式${VAR}即可，但是如果系统参数不存在怎么办？可以使用${VAR:默认值}
     */
    @Override
    protected void initPropertySources() {
        //
        //String var = getEnvironment().getRequiredProperty("VAR");

    }
}
