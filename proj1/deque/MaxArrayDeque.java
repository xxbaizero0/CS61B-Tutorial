package deque;

import deque.ArrayDeque;
import java.util.Comparator;

public class MaxArrayDeque<T> {
    private ArrayDeque<T> arrayDeque;
    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        arrayDeque = new ArrayDeque<>();
        this.comparator = c;
    }

    public T max() {
        if (arrayDeque.isEmpty()) {
            return null;
        }

        T maxElement = arrayDeque.get(0);
        for (int i = 0; i < arrayDeque.size(); i++) {
            if (comparator.compare(arrayDeque.get(i), maxElement) > 0) {
                maxElement = arrayDeque.get(i);
            }
        }

        return maxElement;
    }

    public T max(Comparator<T> c) {
        if (arrayDeque.isEmpty()) {
            return null;
        }

        T maxElement = arrayDeque.get(0);
        for (int i = 0; i < arrayDeque.size(); i++) {
            if (comparator.compare(arrayDeque.get(i), maxElement) > 0) {
                maxElement = arrayDeque.get(i);
            }
        }

        return maxElement;
    }
}
