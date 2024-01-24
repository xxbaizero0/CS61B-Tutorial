/** Array based list.
 *  @author Josh Hug
 */

public class AList {
    /** Creates an empty list. */
    private int[] item;
    private int size;

    public AList() {
        item = new int[100];
        size = 0;
    }

    /** Inserts X into the back of the list. */
    public void addLast(int x) {
        int[] new_item;
        if (size == item.length) {
            new_item = new int[size * 2];
            System.arraycopy(item, 0, new_item, 0, size);
            new_item[size] = x;
            size++;
            item = new_item;
        }else {
            item[size] = x;
            size++;
        }
    }

    /** Returns the item from the back of the list. */
    public int getLast() {
        return item[size - 1];
    }
    /** Gets the ith item in the list (0 is the front). */
    public int get(int i) {
        return item[i];
    }

    /** Returns the number of items in the list. */
    public int size() {
        return size;
    }

    /** Deletes item from back of the list and
     * returns deleted item. */
    public int removeLast() {
        size--;
        return item[size];
    }
} 