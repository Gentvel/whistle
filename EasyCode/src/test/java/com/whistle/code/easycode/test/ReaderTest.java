package com.whistle.code.easycode.test;

import com.whistle.code.easycode.annotation.EasyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.system.SystemProperties;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.util.SystemPropertyUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
@DisplayName("读取测试")
public class ReaderTest {
    private AnnotationConfigApplicationContext applicationContext;

    @BeforeEach
    public void initContainer(){
        applicationContext = new AnnotationConfigApplicationContext();
    }

    @Test
    public void testPackageReader(){
//        String userDirectory = System.getProperty("user.dir");
//        System.out.println(userDirectory);
        Package pkgName = null;
        Package[] packages = Package.getPackages();

        Package aPackage = Package.getPackage("com.whistle.code.easycode.test.easycodetest1");
        /*System.out.println(aPackage);

        EasyCode annotation = aPackage.getAnnotation(EasyCode.class);
        System.out.println(annotation);*/
        Arrays.stream(packages).forEach(pkg->{
            if (pkg.getName().contains("com.whistle")) {
                System.out.println(pkg);
            }
        });
        ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(true);
        Set<String> classes = new HashSet<>();
        componentProvider.findCandidateComponents("com.whistle.code.easycode.test").forEach(beanDefinition -> classes.add(beanDefinition.getBeanClassName()));

        classes.forEach(System.out::println);

    }
}
