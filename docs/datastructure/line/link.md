---
title: 【线性表】系列二 链表
date: 2020-08-10
sidebar: auto
categories:
 - 数据结构
prev: ./sequence
next: false
---

## 一、概述

## 二、单向链表
```java
public class SingleList<E> implements List<E> {

    private int size;
    private final Node<E> first  = new Node(null);


    private static class Node<E>{
        private Node<E> next;
        private E value;
        public Node(E value) {
            this.next = null;
            this.value = value;
        }
        public Node(Node<E> next,E value) {
            this.next = next;
            this.value = value;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public boolean add(E e) {
        if(size==0)
            first.next = new Node<>(e);
        else{
            Node<E> cur = first.next;
            while (cur.next !=null){
                cur = cur.next;
            }
            cur.next = new Node<>(e);
        }
        size++;
        return true;
    }

    public boolean add(int index,E e){
        Node<E> pre = getPre(index);
        Node<E> newNode = new Node<>(e);
        newNode.next = pre.next;
        pre.next =newNode;
        size++;
        return true;
    }

    private Node<E> getPre(int index){
        if(index>size){
            throw new IllegalArgumentException();
        }
        int cur = 1;
        Node<E> pre = first.next;
        while (cur<index-1){
            pre = pre.next;
            cur++;
        }
        return pre;
    }

    @Override
    public E remove(int index) {
        Node<E> pre = getPre(index);
        Node<E> cur = pre.next;
        pre.next = cur.next;
        return cur.value;
    }

    @Override
    public boolean set(int index, E e) {
        Node<E> pre = getPre(index);
        pre.next.value = e;
        return true;
    }

    @Override
    public E get(int index) {
        Node<E> pre = getPre(index);
        return pre.next.value;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("[ ");
        for (Node<E> x = first.next; x != null; x = x.next) {
            stringBuilder.append(x.value+", ");
        }
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
```