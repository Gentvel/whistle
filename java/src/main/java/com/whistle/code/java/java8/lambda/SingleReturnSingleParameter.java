package com.whistle.code.java.java8.lambda;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
@FunctionalInterface
public interface SingleReturnSingleParameter<T> {
    boolean test(T t);
}
