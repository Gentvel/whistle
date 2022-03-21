package com.whistle.code.main.starter.response;

import lombok.Getter;

/**
 * 返回码信息
 *
 * @author Gentvel
 * @version 1.0.0
 */
@Getter
public enum Response {
    /**
     *
     */
    SUCCESS(0,"操作成功"),
    FAILURE_PARAMETER(-1,"参数出错：%s"),
    FAILURE(-2,"发生错误：%s"),
    REQUEST_FAILURE(-3,"请求错误：%s"),
    /**
     *
     */
    IDENTIFY_FAILURE(1001,"识别错误"),
    /**
     *
     */
    WECHAT_FAILURE(2001,"微信服务错误"),;

    private final Integer code;

    private final String msg;

    Response(Integer code,String message){
        this.code = code;
        this.msg = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
