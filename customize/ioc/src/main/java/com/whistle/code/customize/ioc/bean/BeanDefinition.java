package com.whistle.code.customize.ioc.bean;

import java.util.Objects;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public interface BeanDefinition {

    String SCOPE_SINGLETON = "singleton";

    String SCOPE_PROTOTYPE ="prototype";

    Class getBeanClass();
    String getBeanFactoryMethodName();
    String getFactoryBeanName();
    String getBeanName();

    String getScope();
    boolean isSingleton();
    String getInitMethodName();
    String getDestroyMethodName();
    default boolean validate(){
        if(Objects.isNull(this.getBeanClass())){
            return !this.getBeanFactoryMethodName().isBlank() || !this.getFactoryBeanName().isBlank();
        }
        return true;
    }
}
