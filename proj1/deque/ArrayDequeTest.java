package deque;
import edu.princeton.cs.algs4.StdRandom;

import java.io.EOFException;
import java.util.Iterator;
import java.util.Optional;

import org.junit.Test;
import static org.junit.Assert.*;


/** Performs some basic linked list tests. */
public class ArrayDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

        ArrayDeque<String> Ad1 = new ArrayDeque<>();

        assertTrue("A newly initialized LLDeque should be empty", Ad1.isEmpty());
        Ad1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, Ad1.size());
        assertFalse("Ad1 should now contain 1 item", Ad1.isEmpty());

        Ad1.addLast("middle");
        assertEquals(2, Ad1.size());


        Ad1.addLast("back");
        assertEquals(3, Ad1.size());

    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        ArrayDeque<Integer> Ad1 = new ArrayDeque<>();
        // should be empty
        assertTrue("Ad1 should be empty upon initialization", Ad1.isEmpty());

        Ad1.addFirst(10);
        // should not be empty
        assertFalse("Ad1 should contain 1 item", Ad1.isEmpty());

        Ad1.removeFirst();
        // should be empty
        assertTrue("Ad1 should be empty after removal", Ad1.isEmpty());
    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        ArrayDeque<Integer> Ad1 = new ArrayDeque<>();
        Ad1.addFirst(3);

        Ad1.removeLast();
        Ad1.removeFirst();
        Ad1.removeLast();
        Ad1.removeFirst();

        int size = Ad1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);
    }

    @Test
    public void multipleParamTest() {

        ArrayDeque<String>  Ad1 = new ArrayDeque<String>();
        ArrayDeque<Double>  lld2 = new ArrayDeque<Double>();
        ArrayDeque<Boolean> lld3 = new ArrayDeque<Boolean>();

        Ad1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = Ad1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();
    }

    @Test
    /* check if null is return when removing from an empty ArrayDeque. */
    public void emptyNullReturnTest() {

        ArrayDeque<Integer> Ad1 = new ArrayDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, Ad1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, Ad1.removeLast());

    }

    @Test
    public void getTest(){
        ArrayDeque<Integer> Ad1 = new ArrayDeque<>();
        Ad1.addFirst(2);
        Ad1.addFirst(1);
        Ad1.addLast(3);
        assertEquals(1, (int)Ad1.get(0));
        assertEquals(3, (int)Ad1.get(2));
    }
    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {

        ArrayDeque<Integer> Ad1 = new ArrayDeque<Integer>();
        for (int i = 0; i < 100; i++) {
            Ad1.addLast(i);
        }


        for (double i = 0; i < 50; i++) {
            assertEquals("Should have the same value", i, (double) Ad1.removeFirst(), 0.0);
        }

        for (double i = 99; i > 50; i--) {
            assertEquals("Should have the same value", i, (double) Ad1.removeLast(), 0.0);
        }

    }

    @Test
    public void get_null(){
        ArrayDeque<Integer> Ad1 = new ArrayDeque<>();
        for (int i = 0; i < 10; i++) {
            Ad1.addFirst(i);
        }
        assertEquals(9, (int)Ad1.get(0));
        assertEquals(8, (int)Ad1.get(1));
        assertEquals(0, (int)Ad1.get(9));
    }

//    @Test
//    public void equelTest() {
//        LinkedListDeque<Integer> lld1 = LinkedListDeque.of(1, 3, 5, 2);
//        LinkedListDeque<Integer> lld2 = LinkedListDeque.of(1, 3, 5, 2);
//        LinkedListDeque<Integer> lld3 = LinkedListDeque.of(1, 3, 5, 1);
//        assertTrue(lld1.equals(lld2));
//        assertTrue(lld1.equals(lld1));
//        assertFalse(lld1.equals(lld3));
//    }

    @Test
    public void checkAM() {
        ArrayDeque<Integer> studuent = new ArrayDeque<>();
        studuent.addFirst(2);
        studuent.addFirst(2);
        for (int i = 0; i < 9; i++) {
            studuent.addLast(i);
        }
        assertEquals((int) studuent.get(0), 2);
        assertEquals((int) studuent.get(1), 2);
        assertEquals((int) studuent.get(2), 0);
    }

    @Test
    public void checkAdd() {
        ArrayDeque<Integer> studuent = new ArrayDeque<>();
        java.util.ArrayDeque<Integer> solution = new java.util.ArrayDeque<Integer>();
        int N = 5000;
        for (int i = 0; i < N; i++) {
            int operationNumber = StdRandom.uniform(0, 3);
            if (operationNumber == 0) {
                int randVal = StdRandom.uniform(0, 100);
                studuent.addFirst(randVal);
                solution.addFirst(randVal);
                System.out.println("addFirst:(" + randVal + ")");
            } else if (operationNumber == 1) {
//                int randVal = StdRandom.uniform(0, 100);
//                studuent.addLast(randVal);
//                solution.addLast(randVal);
//                System.out.println("addLast:(" + randVal + ")");
//                int size = solution.size();
//                System.out.println("size:(" + size + ")");
            } else if (operationNumber == 2) {
                if (solution.size() == 0) {
                    continue;
                }
                int randVal = StdRandom.uniform(0, 100);
                studuent.addLast(randVal);
                solution.addLast(randVal);
                System.out.println("addLast:(" + randVal + ")");
                int last = solution.getLast();
                if (solution.size() > 0) {
                    assertEquals("No equal", solution.removeLast(), studuent.removeLast());
                    System.out.println("removelast:" + last + ")");
                } else {
                    System.out.println("No enough size");
                }
            }
        }
    }
}

