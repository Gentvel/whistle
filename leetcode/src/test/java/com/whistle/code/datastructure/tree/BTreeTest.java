package com.whistle.code.datastructure.tree;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
@DisplayName("BTreeTest")
public class BTreeTest {
    @Test
    public void testBTree(){
        BTree<Integer, Student> bTree = new BTree<>();
        bTree.put(8,new Student(12,"小米"));
        bTree.put(7,new Student(16,"华为"));
        bTree.put(3,new Student(42,"三星"));
        bTree.put(4,new Student(62,"苹果"));
        bTree.put(6,new Student(72,"雪梨"));
        bTree.put(4,new Student(82,"香蕉"));
        bTree.put(9,new Student(92,"美团"));
        bTree.put(12,new Student(2,"菠萝"));
        System.out.println(bTree.size());
        System.out.println(bTree.get(12));
        System.out.println(bTree.getMinimum());
        //bTree.delete(2);
        System.out.println(bTree.size());
    }
}
