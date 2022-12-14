package com.whistle.starter.response;

import com.whistle.starter.consts.DateConst;
import com.whistle.starter.exception.BizException;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Gentvel
 */
@Getter
@Setter
public final class Result <T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 通用返回状态码
     */
    private String code;
    /**
     * 通用返回信息
     */
    private String msg;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 通用返回时间
     */
    private String time = LocalDateTime.now().format(DateConst.C_FORMATTER);

    /**
     * 初始化Result
     * @param code 返回码
     * @param msg 返回消息
     * @param data 数据
     * @param <T> 数据类型
     * @return {@link Result}
     */
    private static <T> Result<T> initResult(boolean success,String code,String msg,T data){
        Result<T> result = new Result<>();
        result.success = success;
        result.code = code;
        result.msg=msg;
        result.data=data;
        return result;
    }

    /**
     * 成功
     * @param code 返回码
     * @param msg 返回消息
     * @param data 数据
     * @param <T> 数据类型
     * @return {@link Result}
     */
    private static <T>  Result<T> success(String code,String msg,T data){
        return initResult(true,code, msg, data);
    }
    /**
     * 成功
     * @param code 返回码
     * @param msg 返回消息
     * @param data 数据
     * @param <T> 数据类型
     * @return {@link Result}
     */
    public static <T>  Result<T> ok(String code,String msg,T data){
        return success(code, msg, data);
    }

    /**
     * 成功
     * @param msg 返回消息
     * @return {@link Result}
     */
    public static Result<Object> okMsg(String msg){
        return success(ResponseInterface.SUCCESS, msg, null);
    }
    /**
     * 成功
     * @param data 数据
     * @param <T> 数据类型
     * @return {@link Result}
     */
    public static <T>  Result<T> ok(T data){
        return success(ResponseInterface.SUCCESS, ResponseInterface.SUCCESS_MESSAGE, data);
    }

    /**
     * 成功
     * @return {@link Result}
     */
    public static  Result<Object> ok(){
        return success(ResponseInterface.SUCCESS, ResponseInterface.SUCCESS_MESSAGE, null);
    }


    /**
     * 失败
     * @param code 返回码
     * @param msg 返回消息
     * @param data 数据
     * @param <T> 数据类型
     * @return {@link Result}
     */
    private static <T>  Result<T> failure(String code,String msg,T data){
        return initResult(false,code, msg, data);
    }

    /**
     * 失败
     * @param code 返回码
     * @param msg 返回消息
     * @return {@link Result}
     */
    public static Result<Object> fail(String code,String msg){
        return failure(code, msg, null);
    }

    /**
     * 失败
     * @return {@link Result}
     */
    public static Result<Object> fail(BizException e){
        return failure(e.getCode(), e.getMessage(),  null);
    }

    /**
     * 失败
     * @param msg 返回消息
     * @return {@link Result}
     */
    public static Result<Object> fail(String msg){
        return failure(ResponseInterface.FAILURE, msg, null);
    }

    /**
     * 失败
     * @return {@link Result}
     */
    public static  Result<Object> fail(){
        return failure(ResponseInterface.FAILURE, ResponseInterface.FAILURE_MESSAGE, null);
    }


}
