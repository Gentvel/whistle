package com.whistle.code.leetcode.tree;

import com.whistle.code.leetcode.stack.Stack_20;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
@DisplayName("栈测试")
public class TestStack {
    @Test
    @DisplayName("测试第20题")
    public void test20(){
        String string1 = "([";
        String string2 = "([]";
        String string3 = "([}";
        String string4 = "([(";
        String string5 = "(}";
        String string6 = "([])";
//        System.out.println(new Stack_20().isValid(string1));
//        System.out.println(new Stack_20().isValid(string2));
//        System.out.println(new Stack_20().isValid(string3));
//        System.out.println(new Stack_20().isValid(string4));
//        System.out.println(new Stack_20().isValid(string5));
        System.out.println(new Stack_20().isValid(string6));
    }
}
