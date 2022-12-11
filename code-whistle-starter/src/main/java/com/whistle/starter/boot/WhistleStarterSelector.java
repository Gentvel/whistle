package com.whistle.starter.boot;

import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.boot.system.SystemProperties;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.StringUtils;
import org.springframework.util.SystemPropertyUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Gentvel
 */
public class WhistleStarterSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        //使用当前package
        String packageName = "com.whistle.starter";
        //类路径扫描器
        ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(true);
        componentProvider.addExcludeFilter(new AnnotationTypeFilter(Configuration.class));
        //componentProvider.addExcludeFilter(new AnnotationTypeFilter(EnableAutoConfiguration.class));
         Set<String> classes = new HashSet<>();
        componentProvider.findCandidateComponents(packageName).forEach(beanDefinition -> {
            classes.add(beanDefinition.getBeanClassName());
        });
        return classes.toArray(new String[classes.size()]);
    }
}
