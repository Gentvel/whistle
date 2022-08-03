package com.whistle.code.leetcode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * <p>给定一个字符串s，找出其中不含有重复字符的<strong>最长字串<strong>的长度</p>
 * 滑动窗口
 * @author Lin
 */
public class _03LengthOfLongestSubstring {
    public int lengthOfLongestSubstring(String s) {
        if(s==null) {
            return 0;
        }
        if(s.length()==0){
            return 0;
        }
        int max = 0,left = 0,right=0;
        Map<Character,Integer> chars = new HashMap<>();
        int length = s.length();
        while (right<length) {
            char c = s.charAt(right);
            while (chars.containsKey(c)){
                chars.remove(s.charAt(left++));
            }
            chars.put(c,right);
            int temp = right++-left+1;
            max= Math.max(temp, max);
        }
        return max;
    }
}
