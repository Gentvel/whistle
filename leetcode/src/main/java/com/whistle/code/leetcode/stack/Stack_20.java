package com.whistle.code.leetcode.stack;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串，判断字符串是否有效。
 * 有效字符串需满足：
 * 左括号必须用相同类型的右括号闭合。
 * 左括号必须以正确的顺序闭合。
 * 注意空字符串可被认为是有效字符串。
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class Stack_20 {

    private Map<Character, Character> map = new HashMap<>() {
        {
            put(')', '(');
            put('}', '{');
            put(']', '[');
        }
    };

    public boolean isValid(String s) {
        if (s == null) {
            return false;
        }
        if (s.length() % 2 == 1) {
            return false;
        }
        char[] chars = s.toCharArray();
        Stack<Character> stack = new Stack<>();
        for (char aChar : chars) {
            //左括号入栈
            if (map.containsValue(aChar)) {
                stack.push(aChar);
            } else {
                //右括号判断和栈顶元素是否相同，相同则出栈，不相同则入栈
                if (!stack.empty() && map.get(aChar).equals(stack.peek())) {
                    stack.pop();
                } else {
                    stack.push(aChar);
                }
            }
        }
        System.out.println(stack.size());
        return stack.isEmpty();
    }
}
