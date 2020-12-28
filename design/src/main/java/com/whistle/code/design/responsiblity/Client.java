package com.whistle.code.design.responsiblity;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class Client {
    public static void main(String[] args) {
        //组装责任链
        HeadTeacher teacher1 = new HeadTeacher();
        Handler<Integer> teacher2 = new HeadDepartment();
        Handler<Integer> teacher3 = new Dean();

        teacher1.setNext(teacher2);
        teacher1.setNext(teacher3);
        //提交请求
        teacher1.handleRequest(8);
    }
}
