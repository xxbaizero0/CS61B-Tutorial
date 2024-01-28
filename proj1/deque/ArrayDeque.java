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
    public boolean isEmpty() {
        return size <= 0;

    }
    public int size() {
        return size;
    }
    public void printDeque() {
        for (T t : item) {
            if (t != null) {
                System.out.println(t);
            }
        }
    }

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
    public T get(int i) {
        if (i >= size()) {
            return null;
        }
        return item[i];
    }
}
