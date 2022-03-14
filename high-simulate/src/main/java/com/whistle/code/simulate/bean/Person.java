package com.whistle.code.simulate.bean;

import lombok.Builder;

import java.io.Serializable;

/**
 * @author Lin
 * @version 1.0.0
 */
@Builder

public class Person implements Serializable {
    private long id;
    private String name;
    private Integer age;
    private String birthDate;
    private String email;
    private String address;
    private String phone;
    private String photoUrl;
    private String idCard;

    @Override
    public String toString() {
        return String.format("##%s|%d|%s|%s|%s|%s|%s",this.name,this.age,this.birthDate,this.email,this.address,this.phone,this.photoUrl);
    }

}
