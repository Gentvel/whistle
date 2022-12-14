package com.whistle.starter.enums;

import lombok.Getter;

/**
 * @author Gentvel
 */
@Getter
public enum DataEnum {
    //
    STATUS_ENABLE("1"),
    STATUS_DISABLE("0"),
    IS_ADMIN("0"),
    NO_ADMIN("1"),



    ;
    /**
     *
     */
    private final String value;
    DataEnum(String value){
        this.value = value;
    }
}
