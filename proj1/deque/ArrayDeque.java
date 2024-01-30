package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] item;
    private int size;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        item = (T []) new Object[8];
        size = 0;
        nextFirst = 1;
        nextLast = 2;
    }

//    public ArrayDeque(ArrayDeque<T> other) {
//        item = (T []) new Object[8];
//        System.arraycopy(other.item, 0, item, 0, size);
//        size = other.size;
//        nextFirst = other.nextFirst;
//        nextLast = other.nextLast;
//    }

    private int forwardValue(int n) {
        n--;
        if (n < 0) {
            n = item.length - 1;
        }
        return n;
    }

    private int backValue(int n) {
        n++;
        if (n >= item.length) {
            n = 0;
        }
        return n;
    }

    private void resize(int x) {
        T[] a = (T[]) new Object[x];
        if ((size < item.length / 4) && (size > 16)) {
            if ((item.length - nextFirst - 1) < size ) {
                int length = size - (item.length - nextFirst - 1);
                System.arraycopy(item, 0, a, 0, length);
                System.arraycopy(item, nextFirst + 1, a, length + 1, item.length - nextFirst - 1);
                nextFirst = length;
                nextLast = length;
            } else {
                System.arraycopy(item, nextFirst + 1, a, 1, size);
                nextFirst = 0;
                nextLast = 0;
            }
        } else {
            System.arraycopy(item, 0, a, 0, nextFirst + 1);
            int destPost = a.length - item.length + nextFirst + 1;
            int length = item.length - nextFirst - 1;
            System.arraycopy(item, nextLast, a, destPost, length);
            nextFirst = a.length - item.length + nextFirst;
        }
        item = a;
    }

    /** Adds an item of type T to the front of the deque.
     * You can assume that item is never null. */
    @Override
    public void addFirst(T i) {
        if (size == item.length) {
            resize(size * 2);
            while (item[nextFirst] != null) {
                nextFirst = forwardValue(nextFirst);
            }
        }
        item[nextFirst] = i;
        size++;
        nextFirst = forwardValue(nextFirst);
    }

    /** Adds an item of type T to the back of the deque.
     * You can assume that item is never null. */
    @Override
    public void addLast(T i) {
        if (size == item.length) {
            resize(size * 2);
            while (item[nextLast] != null) {
                nextLast = backValue(nextLast);
            }
        }
        item[nextLast] = i;
        size++;
        nextLast = backValue(nextLast);
    }

    /** Returns the number of items in the deque. */
    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.println(item[(nextFirst + i + 1) % item.length]);
        }
    }

    /** Removes and returns the item at the front of the deque.
     * If no such item exists, returns null. */
    @Override
    public T removeFirst() {
        if (size <= 0) {
            return null;
        }
        if ((size < item.length / 4) && (size > 16)) {
            resize(item.length / 4);
        }
        T curValue = item[backValue(nextFirst)];
        item[backValue(nextFirst)] = null;
        nextFirst = backValue(nextFirst);
        size--;
        return curValue;
    }

    /** Removes and returns the item at the back of the deque.
     * If no such item exists, returns null. */
    @Override
    public T removeLast() {
        if (size <= 0) {
            return null;
        }
        if ((size < item.length / 4) && (size > 16)) {
            resize(item.length / 4);
        }

        T curValue = item[forwardValue(nextLast)];
        item[forwardValue(nextLast)] = null;
        nextLast = forwardValue(nextLast);
        size--;
        return curValue;
    }

    /** Gets the ith item in the list (0 is the front). */
    @Override
    public T get(int i) {
        if (i >= size()) {
            return null;
        }
        return item[(nextFirst + i + 1) % item.length];
    }


    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {

        private int wizPos;

        ArrayDequeIterator() {
            wizPos = 0;
        }
        /**
         * @return false if reach the end of deque.
         */
        @Override
        public boolean hasNext() {
            return wizPos < size();
        }

        /**
         * @return
         */
        @Override
        public T next() {
            T nextItem = item[wizPos];
            wizPos++;
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

        ArrayDeque<T> o = (ArrayDeque<T>) obj;
        if (size() != o.size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            if (!this.get(i).equals(o.get(i))) {
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

//    public static <G> ArrayDeque<G> of(G... stuff) {
//        ArrayDeque<G> list = new ArrayDeque<>();
//        for (G i : stuff) {
//            list.addLast(i);
//        }
//        return list;
//    }
}
