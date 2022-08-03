package com.whistle.code.leetcode.tree;


import com.whistle.code.TreeNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
@DisplayName("树结构测试")
public class TestTree {
    @Test
    @DisplayName("测试第94题")
    public void test94(){
        TreeNode root = new TreeNode(1);
        TreeNode t1 = new TreeNode(2);
        TreeNode t2 = new TreeNode(4);
        TreeNode t3 = new TreeNode(3);
        root.right=t1;
        root.left =t2;
        t2.right=t3;
        System.out.println(new Tree_94().inorderTraversal(root));
        System.out.println(new Tree_94().inorderTraversalIteration(root));
    }
}
