package deque;

public interface Deque<T> {

    void addFirst(T i);

    void addLast(T i);

    default boolean isEmpty() {
        return this.size() <= 0;
    }

    int size();

    void printDeque();

    T removeFirst();

    T removeLast();

    T get(int i);
}
