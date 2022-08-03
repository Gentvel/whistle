package com.whistle.code.algorithm;

import com.whistle.code.ListNode;

/**
 * @author Lin
 */
public class ReverseLinkList {

    public ListNode iterator(ListNode listNode){
        ListNode realNext = null,next;
        ListNode current = listNode;
        while (current!=null){
           next = current.next;
           current.next = realNext;
           realNext = current;
           current = next;
        }
        return realNext;
    }
}