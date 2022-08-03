package com.whistle.code.leetcode;

import com.whistle.code.ListNode;

/**
 * 两个非空的链表，表示两个非负的整数，它们每位数字都是按照<strong>逆序</strong>
 * 的方式存储的，并且每个节点只能存储一位数字。
 * <p>将两数相加，并以相同的形式返回一个表示和的<strong>链表<strong></p>
 * <p>可以假设这两个数不会以0开头，也即是不含前导0</p>
 *
 * @author Lin
 */
public class _02SumsTwoLink {

    public ListNode solution(ListNode num1, ListNode num2) {
        ListNode head = new ListNode();
        ListNode current = head;
        int carry = 0;
        while (num1 != null || num2 != null) {
            ListNode listNode = new ListNode();
            current.next = listNode;
            int i = 0;
            if (num1 != null && num2 != null) {
                i = num1.val + num2.val + carry;
                num1 = num1.next;
                num2 = num2.next;
            } else if (num1 == null) {
                i = num2.val + carry;
                num2 = num2.next;
            } else {
                i = num1.val + carry;
                num1 = num1.next;
            }
            if (i >= 10) {
                carry = 1;
                listNode.val = i % 10;
            } else {
                carry = 0;
                listNode.val = i;
            }
            current = listNode;

        }
        if (carry != 0) {
            ListNode listNode = new ListNode(1);
            current.next = listNode;

        }
        return head.next;
    }

}
