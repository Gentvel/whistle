package com.whistle.code.leetcode.tree;

import com.whistle.code.TreeNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 *  二叉树的中序遍历
 *
 *  //给定一个二叉树的根节点 root ，返回它的 中序 遍历。
 *  //输入：root = [1,null,2,3]
 * //输出：[1,3,2]
 *
 *
 * 中序遍历（LDR）是二叉树遍历的一种，也叫做中根遍历、中序周游。
 * 在二叉树中，中序遍历首先遍历左子树，然后访问根结点，最后遍历右子树。
 * @author Gentvel
 * @version 1.0.0
 */
public class Tree_94 {
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> list = new LinkedList<>();
        if(root==null){
            return list;
        }
        if(root.left!=null){
            list.addAll(inorderTraversal(root.left));
        }
        list.add(root.val);
        if(root.right!=null){
            list.addAll(inorderTraversal(root.right));
        }
        return list;
    }

    public List<Integer> inorderTraversalIteration(TreeNode root){
        List<Integer> list = new LinkedList<>();
        if(root==null){
            return list;
        }
        Stack<TreeNode> stack = new Stack<>();
        do{
            //不断往左子树方向走，每走一次就将当前节点保存到栈中
            //这是模拟递归的调用
            if(root!=null) {
                stack.push(root);
                root = root.left;
                //当前节点为空，说明左边走到头了，从栈中弹出节点并保存
                //然后转向右边节点，继续上面整个过程
            } else {
                TreeNode tmp = stack.pop();
                list.add(tmp.val);
                root = tmp.right;
            }
        }while (stack.size()>0);
        return list;
    }
}
