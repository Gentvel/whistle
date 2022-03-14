package com.whistle.code.datastructure.tree;


/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public interface Tree<K,V> {
    /**
     * 向树中插入一个键值对
     * @param key
     * @param value
     */
    void put(K key,V value);


    /**
     * 根据key，从树中找出对应的值
     * @param key
     * @return
     */
    V get(K key);


    /**
     * 根据key，删除树中对应的键值对
     * @param key
     */
    void delete(K key);

    /**
     * 获取树中元素的个数
     * @return
     */
    int size();

    class Node<K,V>{
        private K key;
        private V value;

        private Node<K,V> left;
        private Node<K,V> right;

        private Node<K,V> parent;

        public Node(){

        }

        public Node(K key,V value){
            this(key,value,null,null);
        }

        public Node(K key,V value,Node<K,V> left,Node<K,V> right){
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getLeft() {
            return left;
        }

        public void setLeft(Node<K, V> left) {
            this.left = left;
        }

        public Node<K, V> getRight() {
            return right;
        }

        public void setRight(Node<K, V> right) {
            this.right = right;
        }

        public Node<K, V> getParent() {
            return parent;
        }

        public void setParent(Node<K, V> parent) {
            this.parent = parent;
        }
    }
}
