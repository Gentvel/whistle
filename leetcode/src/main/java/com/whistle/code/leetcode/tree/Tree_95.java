package com.whistle.code.leetcode.tree;

import com.whistle.code.TreeNode;

import java.util.LinkedList;
import java.util.List;

/**
 * 定一个整数 n，生成所有由 1 ... n 为节点所组成的 二叉搜索树 。
 *
 * 输入：3
 * 输出：
 * [
 * [1,null,3,2],
 * [3,2,null,1],
 * [3,1,null,null,2],
 * [2,1,3],
 * [1,null,2,null,3]
 * ]
 *
 * 所谓二分查找树，定义如下：
 *
 * <ul>
 *   <li>若任意节点的左子树不空，则左子树上所有节点的值均小于它的根节点的值；</li>
 *   <li>若任意节点的右子树不空，则右子树上所有节点的值均大于它的根节点的值；</li>
 *   <li>任意节点的左、右子树也分别为二叉查找树；</li>
 *   <li>没有键值相等的节点。</li>
 * </ul>
 * 树 动态规划
 * @author Gentvel
 * @version 1.0.0
 */
public class Tree_95 {
    public List<TreeNode> generateTrees(int n) {
        List<TreeNode> list = new LinkedList<>();
        if(n<=0){
            return list;
        }
        return getAns(1, n);
    }
    private List<TreeNode> getAns(int start, int end) {
        List<TreeNode> ans = new LinkedList<>();
        //此时没有数字，将 null 加入结果中
        if (start > end) {
            ans.add(null);
            return ans;
        }
        //只有一个数字，当前数字作为一棵树加入结果中
        if (start == end) {
            TreeNode tree = new TreeNode(start);
            ans.add(tree);
            return ans;
        }
        //尝试每个数字作为根节点
        for (int i = start; i <= end; i++) {
            //得到所有可能的左子树
            List<TreeNode> leftTrees = getAns(start, i - 1);
            //得到所有可能的右子树
            List<TreeNode> rightTrees = getAns(i + 1, end);
            //左子树右子树两两组合
            for (TreeNode leftTree : leftTrees) {
                for (TreeNode rightTree : rightTrees) {
                    TreeNode root = new TreeNode(i);
                    root.left = leftTree;
                    root.right = rightTree;
                    //加入到最终结果中
                    ans.add(root);
                }
            }
        }
        return ans;
    }
}
