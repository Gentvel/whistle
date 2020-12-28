package com.whistle.code.spring.annotation.bean;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class School {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "I am School Bean，my user is："+user;
    }
}
