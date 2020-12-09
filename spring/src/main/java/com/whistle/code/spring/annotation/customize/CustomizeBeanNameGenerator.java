package com.whistle.code.spring.annotation.customize;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.beans.Introspector;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class CustomizeBeanNameGenerator implements BeanNameGenerator {

    private static final String COMPONENT_ANNOTATION_CLASSNAME = "org.springframework.stereotype.Component";


    /**
     * 生成beanName
     * @see org.springframework.context.annotation.AnnotationBeanNameGenerator
     * @param definition beanDefinition定义信息
     * @param registry 注册信息
     * @return String beanName
     */
    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        String beanName = null;
        if (definition instanceof AnnotatedBeanDefinition) {
            AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) definition;
            //获取注解bean中的元信息
            AnnotationMetadata metadata = annotatedBeanDefinition.getMetadata();
            //获取定义信息中的所有注解
            Set<String> annotationTypes = metadata.getAnnotationTypes();
            for (String aType :annotationTypes){
                //得到注解的所有属性
                AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(aType));
                if(null!=annotationAttributes&&isStereotypeWithNameValue(aType, metadata.getMetaAnnotationTypes(aType), annotationAttributes)){
                    final Object value = annotationAttributes.get("value");
                    if(value instanceof String){
                        String stringValue = (String) value;
                        if(StringUtils.hasLength(stringValue)){
                            if (beanName != null && !stringValue.equals(beanName)) {
                                throw new IllegalStateException("Stereotype annotations suggest inconsistent " +
                                        "component names: '" + beanName + "' versus '" + stringValue + "'");
                            }
                            beanName = stringValue;
                        }
                    }

                }
            }

        }
        return beanName!=null?"my+"+beanName:"my+"+buildDefaultBeanName(definition);
    }

    private String buildDefaultBeanName(BeanDefinition definition) {
        String beanClassName = definition.getBeanClassName();
        Assert.state(beanClassName != null, "No bean class name set");
        String shortClassName = ClassUtils.getShortName(beanClassName);
        return Introspector.decapitalize(shortClassName);
    }

    protected boolean isStereotypeWithNameValue(String annotationType,
                                                Set<String> metaAnnotationTypes, @Nullable Map<String, Object> attributes) {

        boolean isStereotype = annotationType.equals(COMPONENT_ANNOTATION_CLASSNAME) ||
                metaAnnotationTypes.contains(COMPONENT_ANNOTATION_CLASSNAME) ||
                "javax.annotation.ManagedBean".equals(annotationType) ||
                "javax.inject.Named".equals(annotationType);

        return (isStereotype && attributes != null && attributes.containsKey("value"));
    }
}
