package com.whistle.code.main.starter.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Gentvel
 * @version 1.0.0
 */
@Setter
@Getter
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -5522477070015766138L;
    /**
     * 返回码
     */
    private Integer code;
    /**
     * 返回信息
     */
    private String msg;
    /**
     * 返回数据
     */
    private T data;
    /**
     * 操作时间
     */
    private String dateTime;



    private transient static final Result rtn = InnerResult.INSTANCE;

    /**
     * 静态内部类实现单例
     */
    private static class InnerResult {
        private static final Result<?> INSTANCE = new Result<>();
    }

    /**
     * 私有化構造方法
     */
    private Result() {
    }


    /**
     * 返回成功信息
     *
     */
    public static <T> Result<T> success() {
        return setValue(Response.SUCCESS.getCode(), Response.SUCCESS.getMsg(), null);
    }


    /**
     * 返回成功信息
     * 返回碼：code
     * 返回信息： msg
     * 返回數據：data
     * @param data 返回数据
     */
    public static <T> Result<T> success( T data) {
        return setValue(Response.SUCCESS.getCode(), Response.SUCCESS.getMsg(),data);
    }


    /**
     * 返回成功信息
     * 返回碼：code
     * 返回信息： msg
     * 返回數據：data
     *
     * @param msg  返回信息
     * @param data 返回数据
     */
    public static <T> Result<T> success(Integer code,String msg, T data) {
        return setValue(code,msg,data);
    }


    /**
     * 返回失败信息
     * 返回碼：code
     * 返回信息：msg
     *
     * @param code 返回码
     * @param msg  返回信息
     */
    public static Result<?> failure(Integer code, String msg) {
        return setValue(code,msg,null);
    }

    /**
     * 返回失败信息
     * 返回碼：code
     * 返回信息：msg
     *
     * @param code 返回码
     * @param msg  返回信息
     */
    public static Result<?> failure(Integer code, String msg,Object ...object) {
        return setValue(code,String.format(msg,object),null);
    }

    /**
     * 返回失败信息
     */
    public static Result<?> failure(Response response) {
        return setValue(response.getCode(),response.getMsg(),null);
    }

    public static Result<?> failure(Response response,Object ...objects) {
        return setValue(response.getCode(),String.format(response.getMsg(),objects),null);
    }

    /**
     * 发生错误
     * @param msg 错误消息提示
     */
    public static Result<?> error(Integer code,String msg){
        return setValue(code,msg,null);
    }
    /**
     * 发生错误
     * @param msg 错误消息提示
     */
    public static Result<?> error(Integer code,String msg,Object ...object){
        return setValue(code,String.format(msg,object),null);
    }

    private static <T> Result<T> setValue(Integer code, String msg, T data) {
        rtn.code = code;
        rtn.msg = msg;
        rtn.data = data;
        rtn.dateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return rtn;
    }



    @Override
    public String toString() {
        return "Result [code=" + code + ", msg=" + msg + ", data=" + data + "]";
    }
}
