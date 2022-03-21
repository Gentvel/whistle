package com.whistle.code.simulate.reading;

import java.util.List;

/**
 * @author Lin
 * @version 1.0.0
 */
public interface Reading<T> {
    default List<T> read(String path){
        return null;
    }


}
