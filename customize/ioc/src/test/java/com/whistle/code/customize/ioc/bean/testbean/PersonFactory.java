package com.whistle.code.customize.ioc.bean.testbean;

import com.whistle.code.customize.ioc.exception.BeanCreateException;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class PersonFactory {

    public static Person getPerson(){
        return getPerson("kid");
    }

    public static Person getPerson(String type){
        if("kid".equalsIgnoreCase(type)){
            return new Kid();
        }else if("dad".equalsIgnoreCase(type)){
            return new Dad();
        }else{
            System.err.println("could not find type");
            return null;
        }
    }

    public Person getKid(){
        return new Kid();
    }
}
