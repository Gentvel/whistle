package com.whistle.code.customize.ioc.bean;

import java.util.Objects;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class GenericBeanDefinition implements BeanDefinition{

    private Class<?> beanClass;

    private String beanFactoryMethodName;

    private String factoryBeanName;

    private String scope;

    private String beanName;

    private String initMethodName;

    private String destroyMethodName;

    public GenericBeanDefinition() {
    }

    public GenericBeanDefinition(Class<?> beanClass, String beanFactoryMethodName, String factoryBeanName, String scope, String beanName, String initMethodName, String destroyMethodName) {
        this.beanClass = beanClass;
        this.beanFactoryMethodName = beanFactoryMethodName;
        this.factoryBeanName = factoryBeanName;
        this.scope = scope;
        this.beanName = beanName;
        this.initMethodName = initMethodName;
        this.destroyMethodName = destroyMethodName;
    }

    @Override
    public Class<?> getBeanClass() {
        return beanClass;
    }

    @Override
    public String getBeanFactoryMethodName() {
        return beanFactoryMethodName;
    }

    @Override
    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    @Override
    public String getBeanName() {
        return beanName;
    }

    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public boolean isSingleton() {
        return scope.equals(SCOPE_SINGLETON);
    }

    @Override
    public String getInitMethodName() {
        return Objects.isNull(initMethodName)?"":initMethodName;
    }

    @Override
    public String getDestroyMethodName() {
        return Objects.isNull(destroyMethodName)?"":destroyMethodName;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public void setBeanFactoryMethodName(String beanFactoryMethodName) {
        this.beanFactoryMethodName = beanFactoryMethodName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }
}
