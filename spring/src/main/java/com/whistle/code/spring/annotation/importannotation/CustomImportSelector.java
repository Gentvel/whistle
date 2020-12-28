package com.whistle.code.spring.annotation.importannotation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 自定义导入器
 * @author Gentvel
 * @version 1.0.0
 */
public class CustomImportSelector implements ImportSelector {
    /**
     * 实现获取要导入类的字节码
     * 导入的过滤规则使用TypeFilter的AspectJ表达式方式
     * {@link org.springframework.core.type.filter.TypeFilter}
     * @param importingClassMetadata metadata
     * @return string[] 类名
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        //定义扫描包名称
        String[] basePackage = null;
        //判断有@Import注解类上有没有ComponentScan注解
        if(importingClassMetadata.hasAnnotation(ComponentScan.class.getName())){
            Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(ComponentScan.class.getName());
            basePackage= (String[])annotationAttributes.get("basePackages");
        }
        //ComponentScan注解中不存在basePackages属性值，那么就使用当前package
        if(basePackage==null || basePackage.length==0){
            String packageName = null;
            try {
                packageName = Class.forName(importingClassMetadata.getClassName()).getPackageName();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            basePackage = new String[]{packageName};
        }

        //类路径扫描器
        ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(true);
        Set<String> classes = new HashSet<>();
        for (String p : basePackage) {
            componentProvider.findCandidateComponents(p).forEach(beanDefinition -> classes.add(beanDefinition.getBeanClassName()));
        }

        return classes.toArray(new String[classes.size()]);
    }
}
