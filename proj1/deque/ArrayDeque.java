package deque;
import java.util.Iterator;
import java.util.Objects;


public class ArrayDeque<T> implements Deque<T>,Iterable<T> {
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

    public ArrayDeque(ArrayDeque<T> other) {
        item = (T []) new Object[8];
        System.arraycopy(other.item, 0, item, 0, size);
        size = other.size;
        nextFirst = other.nextFirst;
        nextLast = other.nextLast;
    }

    private int ForwardValue(int n) {
        n--;
        if (n < 0) {
            n = item.length - 1;
        }
        return n;
    }

    private int BackValue(int n) {
        n++;
        if (n >= item.length) {
            n = 0;
        }
        return n;
    }

    /** before performing a remove operation that will bring the number of elements in the array under 25% the length of the array,
     * resize the size of the array down. */
    private void resize(int x) {
        T[] a = (T[]) new Object[x];
        if ((size < item.length / 4) && (size > 16)) {
            System.arraycopy(item, nextFirst+1, a, 0, size);
            nextFirst = -1;
            nextLast = size;
        } else {
            System.arraycopy(item, 0, a, 0, nextFirst+1);
            System.arraycopy(item, nextLast, a, a.length - item.length + nextFirst + 1, item.length - nextFirst -1);
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
                nextFirst = ForwardValue(nextFirst);
            }
        }
        item[nextFirst] = i;
        size++;
        nextFirst = ForwardValue(nextFirst);
    }

    /** Adds an item of type T to the back of the deque.
     * You can assume that item is never null. */
    @Override
    public void addLast(T i) {
        if (size == item.length) {
            resize(size * 2);
            while (item[nextLast] != null) {
                nextLast = BackValue(nextLast);
            }
        }
        item[nextLast] = i;
        size++;
        nextLast = BackValue(nextLast);
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
        T curValue = item[BackValue(nextFirst)];
        item[BackValue(nextFirst)] = null;
        nextFirst = BackValue(nextFirst);
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

        T curValue = item[ForwardValue(nextLast)];
        item[ForwardValue(nextLast)] = null;
        nextLast = ForwardValue(nextLast);
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

    @Override
    public Iterator<T> iterator() {
        return new ArrayDeuqeIterator();
    }


    private class ArrayDeuqeIterator implements Iterator<T> {
        private int wizPos;
        private int insideSize;

        ArrayDeuqeIterator() {
            wizPos = (nextFirst + 1) % item.length;
            insideSize = size;
        }
        @Override
        public boolean hasNext() {
            return insideSize > 0;
        }

        @Override
        public T next() {
            T returnT = item[wizPos];
            wizPos = (wizPos + 1) % item.length;
            insideSize -= 1;
            return returnT;
        }

    }



    @Override
    public boolean equals(Object o) {
        //检查地址是否相同
        if (o == this) {
            return true; // 同一个对象
        }
        // 检查o的类型是否为ArrayDeque
        if (o instanceof Deque<?> oas)  { // 是否有相同的泛型类型
            // 检查两个对象的大小是否相等
            if (oas.size() != this.size()) {
                return false;
            }
            // 按顺序检查所有值相等
            Iterator<T> thisIterator = this.iterator();
            Iterator<?> otherIterator = oas.iterator();
            while (thisIterator.hasNext() && otherIterator.hasNext()) {
                T thisItem = thisIterator.next();
                Object otherItem = otherIterator.next();
                if (!(Objects.equals(thisItem, otherItem))) {
                    return false;
                }
            }
            return true;
        } else {
            return false; // 不是相同类型的对象
        }
    }

}
