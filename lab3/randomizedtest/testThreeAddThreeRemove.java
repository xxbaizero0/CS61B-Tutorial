package randomizedtest;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

public class testThreeAddThreeRemove {
    AListNoResizing<Integer> list_1 = new AListNoResizing<>();
    BuggyAList<Integer> list_2 = new BuggyAList<>();
    @Test
    public void test_1() {
        list_1.addLast(4);
        list_1.addLast(5);
        list_1.addLast(6);
        list_2.addLast(4);
        list_2.addLast(5);
        list_2.addLast(6);
        assertEquals(list_1.removeLast(), list_2.removeLast());
        assertEquals(list_1.removeLast(), list_2.removeLast());
        assertEquals(list_1.removeLast(), list_2.removeLast());
    }
}
