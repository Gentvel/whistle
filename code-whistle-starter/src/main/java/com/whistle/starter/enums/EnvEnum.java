package com.whistle.starter.enums;

import lombok.Getter;

/**
 * @author Gentvel
 */
@Getter
public enum EnvEnum {
    //
    DEV("dev"),
    PROD("prod"),



    ;
    /**
     * 环境名称
     */
    private String name;
    EnvEnum(String envName){
        this.name = envName;
    }
}
