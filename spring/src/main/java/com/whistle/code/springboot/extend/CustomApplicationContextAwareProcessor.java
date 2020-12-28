package com.whistle.code.springboot.extend;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

/**
 * 该类本身并没有扩展点，但是该类内部却有6个扩展点可供实现 ，这些类触发的时机在bean实例化之后，初始化之前
 * @see org.springframework.context.support.ApplicationContextAwareProcessor#invokeAwareInterfaces
 * @author Gentvel
 * @version 1.0.0
 */
public class CustomApplicationContextAwareProcessor {


    /**
     * 用于获取EnviromentAware的一个扩展类，这个变量非常有用，
     * 可以获得系统内的所有参数。当然个人认为这个Aware没必要去扩展，因为spring内部都可以通过注入的方式来直接获得。
     * @see EnvironmentAware
     */
    @Component
    static
    class CustomEnvironmentAware implements EnvironmentAware {
        Logger logger = LoggerFactory.getLogger(CustomEnvironmentAware.class);
        @Override
        public void setEnvironment(Environment environment) {
            logger.info("---可以获得系统内的所有参数");
        }
    }

    /**
     * 用于获取StringValueResolver的一个扩展类， StringValueResolver用于获取基于String类型的properties的变量，一般我们都用@Value的方式去获取，
     * 如果实现了这个Aware接口，把StringValueResolver缓存起来，通过这个类去获取String类型的变量，效果是一样的。
     */
    @Component
    static class CustomEmbeddedValueResolverAware implements EmbeddedValueResolverAware {
        Logger logger = LoggerFactory.getLogger(CustomEmbeddedValueResolverAware.class);
        @Override
        public void setEmbeddedValueResolver(StringValueResolver resolver) {
            logger.info("---一般我们都用@Value的方式去获取StringValueResolver,基于String类型的properties的变量");
        }
    }

    /**
     *用于获取ResourceLoader的一个扩展类，ResourceLoader可以用于获取classpath内所有的资源对象，可以扩展此类来拿到ResourceLoader对象。
     */
    @Component
    static class CustomResourceLoaderAware implements ResourceLoaderAware {
        Logger logger = LoggerFactory.getLogger(CustomResourceLoaderAware.class);

        @Override
        public void setResourceLoader(ResourceLoader resourceLoader) {
            logger.info("---可以用于获取classpath内所有的资源对象，可以扩展此类来拿到ResourceLoader对象。");
        }
    }

    /**
     * 用于获取ApplicationEventPublisher的一个扩展类，ApplicationEventPublisher可以用来发布事件，结合ApplicationListener来共同使用。
     * 这个对象也可以通过spring注入的方式来获得。
     */
    @Component
    static class CustomApplicationEventPublisherAware implements ApplicationEventPublisherAware {
        Logger logger = LoggerFactory.getLogger(CustomApplicationEventPublisherAware.class);

        @Override
        public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
            logger.info("---ApplicationEventPublisher可以用来发布事件，结合ApplicationListener来共同使用");
        }
    }

    /**
     * 用于获取MessageSource的一个扩展类，MessageSource主要用来做国际化。
     */
    @Component
    static class CustomMessageSourceAware implements MessageSourceAware{
        Logger logger = LoggerFactory.getLogger(CustomMessageSourceAware.class);

        @Override
        public void setMessageSource(MessageSource messageSource) {
            logger.info("---用于获取MessageSource的一个扩展类，MessageSource主要用来做国际化。");
        }
    }

    /**
     * 用来获取ApplicationContext的一个扩展类，ApplicationContext应该是很多人非常熟悉的一个类了，
     * 就是spring上下文管理器，可以手动的获取任何在spring上下文注册的bean，我们经常扩展这个接口来缓存spring上下文，包装成静态方法。
     * 同时ApplicationContext也实现了BeanFactory，MessageSource，ApplicationEventPublisher等接口，也可以用来做相关接口的事情。
     */
    @Component
    static class CustomApplicationContextAware implements ApplicationContextAware{
        Logger logger = LoggerFactory.getLogger(CustomApplicationContextAware.class);

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            logger.info("---来获取ApplicationContext的一个扩展类");
        }
    }

}
