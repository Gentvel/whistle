package com.whistle.code.customize.ioc.bean;

import com.whistle.code.customize.ioc.bean.testbean.Kid;
import com.whistle.code.customize.ioc.bean.testbean.Person;
import com.whistle.code.customize.ioc.bean.testbean.PersonFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
@DisplayName("BeanFactory 测试案例")
public class BeanFactoryTest {
    static BeanFactory beanFactory = new DefaultBeanFactory();
    @BeforeAll
    @DisplayName("测试BeanFactory new创建对象")
    public static void testBeanFactoryNew(){
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        beanDefinition.setBeanClass(Person.class);
        beanDefinition.setInitMethodName("init");
        beanDefinition.setDestroyMethodName("destroy");

        beanFactory.registerBeanDefinition("person",beanDefinition);
    }

    @BeforeAll
    @DisplayName("测试BeanFactory factoryBean创建对象")
    public static void testBeanFactoryBeanFactory(){
        GenericBeanDefinition factoryBean = new GenericBeanDefinition();
        factoryBean.setBeanName("personFactory");
        factoryBean.setBeanClass(PersonFactory.class);
        factoryBean.setScope(BeanDefinition.SCOPE_SINGLETON);
        beanFactory.registerBeanDefinition(factoryBean);
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        beanDefinition.setBeanName("kid");
        beanDefinition.setFactoryBeanName("personFactory");
        beanDefinition.setBeanFactoryMethodName("getKid");
        beanFactory.registerBeanDefinition(beanDefinition);

    }

    @Test
    @DisplayName("测试对象生成情况")
    public void testBeanFactoryGetBean(){
        Person person = (Person)beanFactory.getBean("person");
        person.doSomething();
        Kid kid = (Kid)beanFactory.getBean("kid");
        PersonFactory personFactory = (PersonFactory)beanFactory.getBean("personFactory");
        kid.doSomething();
        Person kid1 = personFactory.getKid();
        kid1.doSomething();
    }
}
