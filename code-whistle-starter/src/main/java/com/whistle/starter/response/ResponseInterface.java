package com.whistle.starter.response;

/**
 * @author Gentvel
 */
public interface ResponseInterface {
    String SUCCESS="00000";
    String SUCCESS_MESSAGE="请求成功!";
    String FAILURE="00001";
    String FAILURE_MESSAGE="请求失败!";



    /**
     * 获取响应码
     * @return String
     */
    String getCode();
    /**
     * 获取响应信息
     * @return String
     */
    String getMsg();
}
