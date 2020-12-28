package com.whistle.code.spring.boot;


/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
//@RestController
public class TestController {
    //@GetMapping("/test")
    public String hello(){
        System.out.println("hello");
        return "Hello";
    }
}
