---
title: 【树】系列二 二叉搜索树实现
date: 2020-08-11
sidebar: auto
prev: false
next: false
---
## 二叉查找树API设计

**成员变量**
1. private Node root:记录根结点
2. private int N:记录树中元素的个数   

**成员方法**
1. public void put(Key key,Value value):向树中插入一个键值对
2. public Value get(Key key):根据key，从树中找出对应的值
3. public void delete(Key key):根据key，删除树中对应的键值对
4. public int size():获取树中元素的个数

## 二叉查找树实现
插入方法put实现思想：
1. 如果当前树中没有任何一个结点，则直接把新结点当做根结点使用
2. 如果当前树不为空，则从根结点开始：  
 2.1 如果新结点的key小于当前结点的key，则继续找当前结点的左子结点；  
 2.2 如果新结点的key大于当前结点的key，则继续找当前结点的右子结点；  
 2.3 如果新结点的key等于当前结点的key，则树中已经存在这样的结点，替换该结点的value值即可。  
