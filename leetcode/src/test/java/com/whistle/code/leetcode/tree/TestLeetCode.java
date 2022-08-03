package com.whistle.code.leetcode.tree;

import com.whistle.code.ListNode;
import com.whistle.code.leetcode._02SumsTwoLink;
import com.whistle.code.leetcode._03LengthOfLongestSubstring;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ces")
public class TestLeetCode {
    @Test
    public void  test02(){
        ListNode listNode1 = new ListNode(9);
        ListNode listNode2 = new ListNode(9);
        ListNode listNode3 = new ListNode(9);
        ListNode listNode4 = new ListNode(9);
        ListNode listNode5 = new ListNode(9);
        ListNode listNode6 = new ListNode(9);
        ListNode listNode7 = new ListNode(9);
        ListNode listNode8 = new ListNode(9);
        ListNode listNode9 = new ListNode(9);
        ListNode listNode10 = new ListNode(9);
        listNode1.next=listNode2;
        listNode2.next = listNode3;
        listNode3.next = listNode4;
        listNode4.next = listNode5;
        listNode5.next = listNode6;
        listNode6.next = listNode7;
        listNode7.next = listNode8;
        listNode8.next = listNode9;
        listNode9.next = listNode10;


        ListNode listNode11 = new ListNode(9);
        ListNode listNode12 = new ListNode(9);
        ListNode listNode13 = new ListNode(9);
        ListNode listNode14 = new ListNode(9);

        listNode11.next=listNode12;
        listNode12.next=listNode13;
        listNode13.next=listNode14;


        _02SumsTwoLink sumsTwoLink = new _02SumsTwoLink();
        ListNode solution = sumsTwoLink.solution(listNode11, listNode1);

    }




    @Test
    public void question3(){
        _03LengthOfLongestSubstring lengthOfLongestSubstring = new _03LengthOfLongestSubstring();
        int aaaaa = lengthOfLongestSubstring.lengthOfLongestSubstring("bcadbbbbbb");
        System.out.println(aaaaa);
    }
}
