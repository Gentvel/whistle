package com.whistle.code.java.java8.lambda;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
@FunctionalInterface
public interface SingleReturnMultipleParameter<T,V> {
    boolean test(T t,V v);
}
