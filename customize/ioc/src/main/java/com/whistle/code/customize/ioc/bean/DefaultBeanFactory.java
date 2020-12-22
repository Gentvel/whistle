package com.whistle.code.customize.ioc.bean;

import com.whistle.code.customize.ioc.exception.BeanCreateException;
import com.whistle.code.customize.ioc.exception.BeanDefinitionRegistryException;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Gentvel
 * @version 1.0.0
 */
public class DefaultBeanFactory implements BeanFactory, BeanDefinitionRegistry , Closeable {
    private Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>(256);

    private Map<String, Object> singletonBeans = new ConcurrentHashMap<>(256);

    private ReentrantLock lock = new ReentrantLock();


    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionRegistryException {
        /*
        1.如何储存beanDefinition->Map
        2.beanName重名怎么办->Spring中不允许重名，重名抛异常，可以使用参数spring.main.allow-bean-definition-overriding:true来允许覆盖
        3.要完成哪些事情
         */

        Objects.requireNonNull(beanName, "注册beanName不能为空！");
        Objects.requireNonNull(beanDefinition, "注册bean定义信息不能为空");

        if (!beanDefinition.validate()) {
            throw new BeanDefinitionRegistryException("名字为[ " + beanName + " ]的bean定义信息不合法：" + beanDefinition);
        }

        if (this.containBeanDefinition(beanName)) {
            throw new BeanDefinitionRegistryException("名字为[ " + beanName + " ]的bean定义信息已存在！" + beanDefinition);
        }
        beanDefinitions.put(beanName, beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitions.get(beanName);
    }

    @Override
    public boolean containBeanDefinition(String beanName) {
        return beanDefinitions.containsKey(beanName);
    }

    @Override
    public Object getBean(String beanName) {
        Objects.requireNonNull(beanName, "beanName不能为空！");
        Object instance = singletonBeans.get(beanName);
        if (Objects.nonNull(instance)) {
            return instance;
        }

        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        if (beanDefinition.isSingleton()) {
            lock.lock();
            instance = singletonBeans.get(beanName);
            if (Objects.isNull(instance)) {
                instance = doGetBean(beanDefinition);
                singletonBeans.put(beanName, instance);
            }
            lock.unlock();
        } else {
            instance = doGetBean(beanDefinition);
        }

        return instance;
    }

    protected Object doGetBean(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Object instance;
        if (beanClass != null) {
            //使用new创建
            if (Objects.isNull(beanDefinition.getBeanFactoryMethodName())) {
                instance = createBeanByConstructor(beanClass);
            } else {
                //使用beanFactory创建
                instance = createBeanByBeanFactory(beanDefinition);
            }
        } else {
            //使用factoryBean创建
            instance = createBeanByFactoryBean(beanDefinition);
        }
        if (Objects.isNull(instance)) {
            throw new BeanCreateException("Bean [ " + beanDefinition.getBeanName() + " ]创建失败！");
        }
        doInit(beanDefinition, instance);
        return instance;
    }

    protected void doInit(BeanDefinition beanDefinition, Object instance) {
        String initMethodName = beanDefinition.getInitMethodName();
        if (!initMethodName.isBlank()) {
            try {
                Method method = instance.getClass().getMethod(initMethodName);
                method.setAccessible(true);
                method.invoke(instance);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private Object createBeanByFactoryBean(BeanDefinition beanDefinition) {
        Object factoryBean = this.doGetBean(getBeanDefinition(beanDefinition.getFactoryBeanName()));
        String beanFactoryMethodName = beanDefinition.getBeanFactoryMethodName();
        try {
            Method method = factoryBean.getClass().getMethod(beanFactoryMethodName);
            method.setAccessible(true);
            return method.invoke(factoryBean);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object createBeanByBeanFactory(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        try {
            Method method = beanClass.getMethod(beanDefinition.getBeanFactoryMethodName());
            method.setAccessible(true);
            return method.invoke(beanClass);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object createBeanByConstructor(Class<?> beanClass) {
        try {
            Constructor<?> constructor = beanClass.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void close() throws IOException {
        //执行单例销毁
        beanDefinitions.entrySet().parallelStream().forEach(entry->{
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            if(beanDefinition.isSingleton()&&!beanDefinition.getDestroyMethodName().isBlank()){
                Object o = singletonBeans.get(beanName);
                try {
                    Method method = o.getClass().getMethod(beanDefinition.getDestroyMethodName());
                    method.setAccessible(true);
                    method.invoke(o);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
        //原型怎么办？
    }
}
