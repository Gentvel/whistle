package com.whistle.code.easycode.importor;

import com.whistle.code.easycode.annotation.EasyCode;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.awt.*;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.List;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class ClassImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        String[] basePackage = null;
        String packageName = null;
        Set<Package> packages = new HashSet<>();
        Package[] pkgs = Package.getPackages();
        List<String> includes = new ArrayList<>();
        List<String> excludes = new ArrayList<>();
        try {
            packageName = Class.forName(importingClassMetadata.getClassName()).getPackageName();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        assert packageName != null;
        String basePackageName = packageName.substring(0,packageName.lastIndexOf("."));
        for (Package pkg : pkgs) {
            if(pkg.getName().contains(basePackageName)){
                packages.add(pkg);
            }
        }

        for (Package aPackage : packages) {
            Annotation[] annotations = aPackage.getAnnotations();
            EasyCode annotation = aPackage.getAnnotation(EasyCode.class);
            System.out.println(aPackage+" : "+ annotation);
//            for (Annotation annotation : annotations) {
//                if(annotation instanceof EasyCode){
//                    if(null!=(EasyCode)annotation.excludes()&&annotation.excludes().length>0){
//                        excludes.addAll(Arrays.asList(annotation.excludes()));
//                    }
//
//                    if(null!=annotation.includes()&&annotation.includes().length>0){
//                        includes.addAll(Arrays.asList(annotation.includes()));
//                    }
//                }
//            }

        }
        //判断有@Import注解类上有没有ComponentScan注解
        if(importingClassMetadata.hasAnnotation(ComponentScan.class.getName())){
            Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(ComponentScan.class.getName());
            basePackage= (String[])annotationAttributes.get("basePackages");
        }

        //ComponentScan注解中不存在basePackages属性值，那么就使用当前package
        if(basePackage==null || basePackage.length==0){
            basePackage = new String[]{packageName};
        }

        //类路径扫描器
        ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(true);
        Set<String> classes = new HashSet<>();
        for (String p : basePackage) {
            componentProvider.findCandidateComponents(p).forEach(beanDefinition -> classes.add(beanDefinition.getBeanClassName()));
        }
        if(!includes.isEmpty()){
            classes.addAll(includes);
        }

        //return classes.toArray(new String[classes.size()]);
        return new String[0];
    }
}
