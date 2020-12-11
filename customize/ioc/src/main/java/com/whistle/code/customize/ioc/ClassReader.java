package com.whistle.code.customize.ioc;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
@FunctionalInterface
public interface ClassReader {
    /**
     * 读取类
     * @return {@see Class}
     */
    Class<?>[] read();
}
