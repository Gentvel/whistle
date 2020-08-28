package com.whistle.code.foundation.annotation;

import com.whistle.code.CommonTest;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Description: <br>
 *
 * @auther: Gentvel
 * @since: 1.0
 * @see: com.whistle.code.foundation.annotation.AnnotationTest
 */
public class AnnotationTest extends CommonTest {
    @Test
    public void testGameAnnotation() throws ClassNotFoundException, NoSuchMethodException {
        Class<?> aClass = Class.forName("com.whistle.code.foundation.annotation.PlayGame");
        Annotation[] annotations = aClass.getAnnotations();
        Arrays.stream(annotations).forEach(System.out::println);
        Game[] annotationsByType = aClass.getAnnotationsByType(Game.class);
        Arrays.stream(annotationsByType).forEach(anno->{
            System.out.println(anno.value());
        });
        Method[] declaredMethods = aClass.getDeclaredMethods();
        Arrays.stream(declaredMethods).forEach(System.out::println);
        Method play = aClass.getDeclaredMethod("play");
        System.out.println(play);
//        Game annotation = aClass.getAnnotation(Game.class);
//        System.out.println(annotation.value());
    }
}
