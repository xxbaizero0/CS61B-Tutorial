package bstmap;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>, Iterable<K> {
    private Node root;             // root of BST

    private class Node {
        private K key;           // sorted by key
        private V val;         // associated data
        private Node left, right;  // left and right subtrees
        private int size;          // number of nodes in subtree

        public Node(K key, V val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }

    /**
     * Initializes an empty symbol table.
     */
    public BSTMap() {
    }
    @Override
    public void clear() {
        root.size = 0;
//        clear(root);
        root = null;
    }

//    private Node clear(Node node) {
//        if (node == null) {
//            return null;
//        }
//        if (node.left != null) {
//            node.left = clear(node.left);
//        }
//        if (node.right != null) {
//            node.right = clear(node.right);
//        }
//        return null;
//    }
    @Override
    public boolean containsKey(K key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return conainsKey(root, key);
    }

    private boolean conainsKey(Node n, K key) {
        if (n == null) {
            return false;
        }
        int cmp = key.compareTo(n.key);
        if (cmp > 0) {
            return conainsKey(n.right, key);
        } else if (cmp < 0) {
            return conainsKey(n.left, key);
        } else {
            return true;
        }
    }

    @Override
    public V get(K key) {
        return get(root, key);
    }

    private V get(Node n, K key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        if (n == null) return null;
        int cmp = key.compareTo(n.key);
        if (cmp > 0) {
            return get(n.right, key);
        } else if (cmp < 0) {
            return get(n.left, key);
        } else {
            return n.val;
        }

    }

    @Override
    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return x.size;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        root = put(root, key, value);
    }

    private Node put(Node node, K key, V value) {
        if (node == null) {
            return new Node(key, value, 1);
        }
        int cmp = key.compareTo(node.key);
        if (cmp > 0) {
            node.right = put(node.right, key, value);
        } else if (cmp < 0) {
            node.left = put(node.left, key, value);
        } else {
            node.val = value;
        }
        node.size = 1 + size(node.right) + size(node.left);
        return node;
    }

    public void PrintInOrder() {
        throw new IllegalArgumentException("argument to contains() is null");
    }
    @Override
    public Set<K> keySet() {
        throw new IllegalArgumentException("argument to contains() is null");
    }

    @Override
    public V remove(K key) {
        throw new IllegalArgumentException("argument to contains() is null");
    }

    @Override
    public V remove(K key, V value) {
        throw new IllegalArgumentException("argument to contains() is null");
    }

    @Override
    public Iterator<K> iterator() {
        throw new IllegalArgumentException("argument to contains() is null");
    }

//    private class MapIterator implements Iterator<K> {
//
//        /**
//         * Returns {@code true} if the iteration has more elements.
//         * (In other words, returns {@code true} if {@link #next} would
//         * return an element rather than throwing an exception.)
//         *
//         * @return {@code true} if the iteration has more elements
//         */
//        int size = size();
//        MapIterator() {
//
//        }
//        @Override
//        public boolean hasNext() {
//
//        }
//
//        /**
//         * Returns the next element in the iteration.
//         *
//         * @return the next element in the iteration
//         */
//        @Override
//        public K next() {
//            return null;
//        }
//    }
}
