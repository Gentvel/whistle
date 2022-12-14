package com.whistle.oauth.bean;

import com.whistle.starter.response.ResponseInterface;
import lombok.Getter;

/**
 * @author Gentvel
 */
public enum RespEnum implements ResponseInterface {
    /**
     * 10000-19999 用户异常
     */
    USER_EXISTS("1000", "当前用户名已存在！"),
    USER_ADD_ERROR("1001", "用户新增失败！"),



    ;

    private final String code;
    private final String msg;

    RespEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
