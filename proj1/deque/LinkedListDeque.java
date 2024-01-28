package deque;
import java.util.Iterator;

public class LinkedListDeque<T> {

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
    public void addFirst(T item) {
        sentinel.next = new Node(item, sentinel, sentinel.next);
        size += 1;
    }
    public void addLast(T item) {
        sentinel.prev = new Node(item, sentinel.prev, sentinel);
        sentinel.prev.prev.next = sentinel.prev;
        size += 1;
    }

    public boolean isEmpty() {
        return size() <= 0;
    }

    public int size() {
        return size;
    }

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
}
