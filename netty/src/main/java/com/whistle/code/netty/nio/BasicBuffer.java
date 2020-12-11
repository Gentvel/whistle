package com.whistle.code.netty.nio;

import java.nio.IntBuffer;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class BasicBuffer {
    public static void main(String[] args) {
        final IntBuffer allocate = IntBuffer.allocate(10);
        for (int i=0;i<allocate.capacity();i++){
            allocate.put(i+1);
        }
        allocate.flip();
        while (allocate.hasRemaining()){
            System.out.println(allocate.get());
        }
    }
}
