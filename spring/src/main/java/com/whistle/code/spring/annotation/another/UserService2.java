package com.whistle.code.spring.annotation.another;

import org.springframework.stereotype.Service;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
@Service
public class UserService2 {
    public void addUser(){
        System.out.println("user added .");
    }
}