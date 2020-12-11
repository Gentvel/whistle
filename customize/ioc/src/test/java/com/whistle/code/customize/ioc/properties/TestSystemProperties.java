package com.whistle.code.customize.ioc.properties;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */

public class TestSystemProperties {
    @Test
    public void testSystemProperties(){
        Properties p = System.getProperties();
        Iterator it = p.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry entry = (Map.Entry) it.next();
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }
    }
}
