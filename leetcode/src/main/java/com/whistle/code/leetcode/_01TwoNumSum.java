package com.whistle.code.leetcode;

import java.util.HashMap;

/**
 * 给定一个整数数组nums和一个整数目标值target，请在该数组中找出<strong>和为目标值</strong>的两个整数，
 * 并返回他们的数组下标。<p>可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现</p>
 * 可以按照任意顺序返回答案
 *
 * @author Lin
 */
public class _01TwoNumSum {
    public int[] twoSum(int[] nums , int target){
        int length = nums.length;
        for (int i = 0; i < length-1; i++) {
            for (int j = i+1; j < length; j++) {
                if(target-nums[i]==nums[j]){
                    return new int[]{i,j};
                }
            }
        }
        return new int[0];
    }


    public int[] twoSum1(int[] nums, int target){
        int length = nums.length;
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        for (int i = 0; i < length; i++) {
            if(hashMap.containsKey(target-nums[i])){
                return new int[]{i,hashMap.get(target-nums[i])};
            }
            hashMap.put(nums[i],i);
        }
        return new int[0];
    }
}
