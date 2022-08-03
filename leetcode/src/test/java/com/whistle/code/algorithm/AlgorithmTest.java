package com.whistle.code.algorithm;

import com.whistle.code.ListNode;
import org.junit.jupiter.api.Test;

public class AlgorithmTest {
    @Test
    public void testReverseListNode(){
        ListNode listNode5 = new ListNode(5,null);
        ListNode listNode4 = new ListNode(4,listNode5);
        ListNode listNode3 = new ListNode(3,listNode4);
        ListNode listNode2 = new ListNode(2,listNode3);
        ListNode listNode1 = new ListNode(1,listNode2);
        ListNode iterator = new ReverseLinkList().iterator(listNode1);
        System.out.println(iterator);
    }
}
