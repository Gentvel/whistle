package com.whistle.code.datastructure.tree;

/**
 * 实现Compare接口与Comparator接口的类，都是为了对象实例数组排序的方便，因为可以直接调用
 * java.util.Arrays.sort(对象数组名称),可以自定义排序规则。
 * 排序实现的原理都是基于红黑二叉树原理实现的。
 * 不同之处：
 * 1 排序规则实现的方法不同
 * Comparable接口的方法：compareTo(Object o)
 * Comparator接口的方法：compare(T o1, To2)
 * 2 类设计前后不同
 * Comparable接口用于在类的设计中使用；设计初期，就实现这个接口，指定排序方式。
 * Comparator接口用于类设计已经完成，还想排序（Arrays）。
 *
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class BTree<K extends Comparable<K>,V> implements Tree<K,V>{

    private Tree.Node<K,V> root;
    private int size;


    public BTree(){

    }

    /**
     * 1.判断当前节点的key大还是小，大了就往左子树递归，小了就往右子树递归
     * 2.当左子树不存在时，生成一个左子树，并返回，右子树同理
     * 3.相等时覆盖当前值
     * @param node
     * @param key
     * @param value
     * @return
     */
    private void put(Node<K,V> node, K key, V value){

        int i = key.compareTo(node.getKey());
        if(i==0){
            node.setValue(value);
        }
        if(i<0){
            if(node.getLeft()==null){
                Node<K, V> kvNode = new Node<>(key,value);
                kvNode.setParent(node);
                node.setLeft(kvNode);
            }else{
                put(node.getLeft(), key, value);
            }
        }
        if(node.getRight()==null){
            Node<K, V> kvNode = new Node<>(key,value);
            kvNode.setParent(node);
            node.setRight(kvNode);
        }else{
            put(node.getRight(),key,value);
        }

    }

    @Override
    public void put(K key, V value) {
        //判断是否存在根节点
        if(size == 0){
            root = new Node<>(key,value);
        }else{
            put(root,key,value);
        }
    }

    private Node<K,V> get(Node<K,V> node,K key){
        if(node==null)
            return null;
        int i = key.compareTo(node.getKey());
        if(i==0) return node;
        if(i<0){
           if(node.getLeft()==null) return null;
           return get(node.getLeft(),key);
        }
        if(node.getRight()==null) return null;
        return get(node.getRight(),key);
    }

    @Override
    public V get(K key) {
        Node<K, V> kvNode = get(root, key);
        if(kvNode==null)
            return null;
        return kvNode.getValue();
    }


    private void removeCurrentNode(Node<K,V> node){
        if(node==null){
            throw new IllegalArgumentException("tree is empty , could not be remove element");
        }
        if(root==node)
            root=null;
        else {
            Node<K, V> parent = node.getParent();
            if(parent.getLeft()!=null&&parent.getLeft()==node){
                parent.setLeft(null);
            }else {
                parent.setRight(null);
            }
            node.setParent(null);
            node.setRight(null);
            node.setLeft(null);
        }
    }



    @Override
    public void delete(K key) {
        Node<K, V> remove = get(root, key);
        if(remove==null){
            throw new IllegalArgumentException("key is not exist");
        }
        //刚好删除的为当前节点
        if(remove.getRight()==null&&remove.getLeft()==null){
            removeCurrentNode(remove);
        }
        //左右子节点都不为空
        if(remove.getLeft()!=null&&remove.getRight()!=null){
            Node<K, V> minimum = getMin(remove);
            removeCurrentNode(minimum);
            minimum.setLeft(remove.getLeft());
            minimum.setRight(remove.getRight());
            minimum.setParent(remove.getParent());
            removeCurrentNode(remove);
        }

        //存在左子节点
        if(remove.getLeft()!=null&&remove.getRight()==null){
            Node<K, V> left = remove.getLeft();
            left.setParent(remove.getParent());
            removeCurrentNode(remove);
        }

        //存在右子节点
        if(remove.getRight()!=null&&remove.getLeft()==null){
            Node<K, V> minimum = getMin(remove);
            if(minimum==remove){
                Node<K, V> right = remove.getRight();
                right.setParent(remove.getParent());
            }else{
                removeCurrentNode(minimum);
                minimum.setRight(remove.getRight());
                minimum.setParent(remove.getParent());
            }
            removeCurrentNode(remove);
        }
        size--;
    }



    private Node<K,V> getMax(Node<K,V> node){
        if(node.getRight()==null)
            return node;
        else
            return getMax(node.getRight());
    }

    private Node<K,V> getMin(Node<K,V> node){
        if(node.getLeft()==null)
            return node;
        else
            return getMin(node.getLeft());
    }
    public V getMaximum(){
        return getMax(root).getValue();
    }

    public V getMinimum(){
        return getMin(root).getValue();
    }


    @Override
    public int size() {
        return size;
    }
}
