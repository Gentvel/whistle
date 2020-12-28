package com.whistle.code.spring.annotation.bean;

import org.springframework.stereotype.Service;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
@Service
public class UserService {
    public void addUser(){
        System.out.println("user added .");
    }
}
