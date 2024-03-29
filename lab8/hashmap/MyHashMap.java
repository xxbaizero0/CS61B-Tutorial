package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!

    private int size;

    private double loadFactor;
    private int capacity;

    /** Constructors */
    public MyHashMap() {
        this(16, 0.75);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, 0.75);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        capacity = initialSize;
        buckets = createTable(capacity);
        loadFactor = maxLoad;
        size = 0;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return (Collection<Node>[]) new Collection[tableSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    /**
     * Removes all of the mappings from this map.
     */
    @Override
    public void clear() {
        MyHashMap<K, V> newTable = new MyHashMap<>();
        size = newTable.size();
        capacity = newTable.capacity;
        loadFactor = newTable.loadFactor;
        buckets = newTable.buckets;
    }

    /**
     * Returns true if this map contains a mapping for the specified key.
     *
     * @param key
     */
    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null in containsKey() function");
        }
        return get(key) != null;
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     *
     * @param key
     */
    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("the key is null in get() function");
        }
        Collection<Node> bucket = getBucket(key);
        if (bucket != null) {
            for (Node node : bucket) {
                if (node.key.equals(key)) {
                    return node.value;
                }
            }
        }
        return null;
    }

    /**
     * Returns the number of key-value mappings in this map.
     */
    private int hashCode(K num) {
        return Math.abs(num.hashCode()) % capacity;
    }
    private Collection<Node> getBucket(K k) {
        int index = hashCode(k);
        return buckets[index];
    }
    @Override
    public int size() {
        return size;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     *
     * @param key
     * @param value
     */
    @Override
    public void put(K key, V value) {
        Collection<Node> bucket = getBucket(key);
        if (bucket == null) {
            // if bucket dont exist
            int index = hashCode(key);
            buckets[index] = createBucket();
            bucket = buckets[index];
        }
        putIn(bucket, key, value);
        double load = (double) size() /capacity;
        if (load > loadFactor) {
            resize(2 * capacity);
        }
    }

    private void resize(int size) {
        capacity = size;
        Collection<Node>[] newBuckets = createTable(size);
        for (Collection<Node> bucket : buckets) {
            if (bucket != null) {
                for (Node node : bucket) {
                    int index = hashCode(node.key);
                    if (newBuckets[index] == null) {
                        newBuckets[index] = createBucket();
                    }
                    newBuckets[index].add(node);
                }
            }
        }
        buckets = newBuckets;
    }

    private void putIn(Collection<Node> bucket, K key, V value) {
        for (Node x : bucket) { // iterate the bucket
            if (x.key.equals(key)) { // find the exact key
                x.value = value;
                return;
            }
        }
        bucket.add(new Node(key, value));
        size++;
    }
    /**
     * Returns a Set view of the keys contained in this map.
     */
    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        for (Collection<Node> bucket : buckets) {
            if (bucket != null) {
                for (Node node : bucket) {
                    keySet.add(node.key);
                }
            }
        }
        return keySet;
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException.
     *
     * @param key
     */
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.
     *
     * @param key
     * @param value
     */
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<K> iterator() {
        return null;
    }

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */

}
