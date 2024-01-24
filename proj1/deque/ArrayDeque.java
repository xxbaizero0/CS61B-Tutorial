package deque;

public class ArrayDeque<T> {
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

    public ArrayDeque(ArrayDeque other) {
        item = (T []) new Object[8];
        System.arraycopy(other.item, 0, item, 0, size);
        size = other.size;
        nextFirst = other.nextFirst;
        nextLast = other.nextLast;
    }

    private int next_value(int n) {
        n--;
        if(n < 0) n = item.length;
        return n;
    }

    private int back_value(int n) {
        n++;
        if(n > item.length) n = 0;
        return n;
    }

    private void resize(int x) {
        T[] a = (T[]) new Object[x];
        System.arraycopy(item, 0, a, 0, size);
        item = a;
    }
    public void addFirst(T i) {
        if (size == item.length) {
            resize(size * 2);
        }
        item[nextFirst] = i;
        size++;
        nextFirst = next_value(nextFirst);
    }

    public void addLast(T i) {
        if (size == item.length) {
            resize(size * 2);
        }
        item[nextLast] = i;
        size++;
        nextLast = next_value(nextLast);
    }
    public boolean isEmpty() {
        return size <= 0;

    }
    public int size() {
        return size;
    }
    public void printDeque() {
        for (T t : item) {
            if (t != null) System.out.println(t);
        }
    }

    private void save_space() {
        int rate = size/item.length;
        if (rate < 0.25) {
            T[] a = (T[]) new Object[item.length/2];
            System.arraycopy(item, 0, a, 0, a.length);
            item = a;
        }
    }

    public T removeFirst() {
        save_space();
        T cur_value = item[back_value(nextFirst)];
        item[back_value(nextFirst)] = null;
        nextFirst = back_value(nextFirst);
        return cur_value;
    }
    public T removeLast() {
        save_space();
        T cur_value = item[back_value(nextLast)];
        item[back_value(nextLast)] = null;
        nextLast = back_value(nextLast);
        return cur_value;
    }

    /** Gets the ith item in the list (0 is the front). */
    public T get(int i) {
        return item[i];
    }
}
