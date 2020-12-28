package com.whistle.code.design.responsiblity;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public interface Handler<T> {
    void handleRequest(T t);
}
