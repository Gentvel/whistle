package com.whistle.starter.response;

/**
 * @author Gentvel
 */
public interface ResponseInterface {
    String SUCCESS="0";
    String SUCCESS_MESSAGE="请求成功!";
    String FAILURE="0001";
    String FAILURE_MESSAGE="请求失败!";

    String SYS_ERROR="0002";
    String SYS_ERROR_MESSAGE="系统开小差了，请检查网络或稍后重试！";

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
