package deque;
import java.util.Iterator;

import java.util.List;
import java.util.Objects;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {

    public class Node {
        private T item;
        private Node next;
        public Node prev;
        Node(T i, Node p, Node n) {
            item = i;
            prev = p;
            next = n;
        }
    }

    private int size;
    private final Node sentinel;
    public LinkedListDeque() {
        sentinel = new Node(null ,null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public LinkedListDeque(T i) {
        sentinel = new Node(i, null, null);
        sentinel.next = new Node(i, sentinel, sentinel);
        sentinel.prev = sentinel.next;
        size = 1;
    }

    /** Adds an item of type T to the front of the deque.
     * You can assume that item is never null. */
    @Override
    public void addFirst(T item) {
        sentinel.next = new Node(item, sentinel, sentinel.next);
        size += 1;
    }

    /** Adds an item of type T to the back of the deque.
     * You can assume that item is never null. */
    @Override
    public void addLast(T item) {
        sentinel.prev = new Node(item, sentinel.prev, sentinel);
        sentinel.prev.prev.next = sentinel.prev;
        size += 1;
    }

    /** Returns the number of items in the deque. */
    @Override
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line. */
    @Override
    public void printDeque() {
        int i = size();
        Node p = sentinel;
        while (i > 0) {
            System.out.print(p.next.item);
            System.out.print(" ");
            p = p.next;
            i--;
        }
    }

    /** Removes and returns the item at the front of the deque.
     * If no such item exists, returns null. */
    @Override
    public T removeFirst() {
        if (size <= 0) {
            return null;
        }
        size--;
        T item = sentinel.next.item;
        sentinel.next.next.prev = sentinel.next.prev;
        sentinel.next = sentinel.next.next;
        if (size == 0) {
            return item;
        }
        return item;
    }

    /** Removes and returns the item at the back of the deque.
     * If no such item exists, returns null. */
    @Override
    public T removeLast() {
        if (size <= 0) {
            return null;
        }
        size--;
        T item = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        if (size == 0) {
            return item;
        }
        return item;
    }

    /** Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque! */
    @Override
    public T get(int index) {
        if (index > size()) {
            return null;
        }
        Node p = sentinel;
        while (index > 0) {
            p = p.next;
            index--;
        }
        return p.item;
    }

    private T getRecursivehelper(int index, Node s) {
        /*
        get函数的递归方式
        */
        if (index == 0) {
            return s.item;
        } else {
            return getRecursivehelper(index - 1, s.next);
        }
    }
    /** Same as get, but uses recursion. */
    public T getRecursive(int index) {
        return getRecursivehelper(index, sentinel.next);
    }

    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {

        Node p;

        public LinkedListDequeIterator() {
            p = sentinel.next;
        }

        @Override
        public boolean hasNext() {
            return p != sentinel;
        }

        @Override
        public T next() {
            T nextItem = p.item;
            p = p.next;
            return nextItem;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        LinkedListDeque<T> o = (LinkedListDeque<T>) obj;
        if (size() != o.size()) {
            return false;
        }
        Iterator<T> l1 = this.iterator();
        Iterator<T> l2 = o.iterator();
        for (int i = 0; i < size(); i++) {
            if (l1.next() != l2.next()) {
                return false;
            }
        }
        return true;
    }

//    @Override
//    public String toString() {
//        List<String> listOfItems = new ArrayList<>();
//        for (T x : this) {
//            listOfItems.add(x.toString());
//        }
//        return "{" + String.join(",", listOfItems) + "}";
//    }

    public static <G> LinkedListDeque<G> of(G... stuff) {
        LinkedListDeque<G> list = new LinkedListDeque<>();
        for (G i : stuff) {
            list.addLast(i);
        }
        return list;
    }
}
