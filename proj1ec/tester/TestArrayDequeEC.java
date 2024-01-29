package tester;
import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {
    public boolean compareDeques(StudentArrayDeque<Integer> student, ArrayDequeSolution<Integer> solution) {
        if (student.size() != solution.size()) {
            return false;
        }
        for (int i = 0; i < student.size(); i++) {
            if (solution.get(i) != student.get(i)) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void checkAdd() {
        StudentArrayDeque<Integer> studuent = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solution = new ArrayDequeSolution<>();
        int N = 50000;
        for (int i = 0; i < N; i++) {
            int operationNumber = StdRandom.uniform(0, 3);
            if (operationNumber == 0) {
                int randVal = StdRandom.uniform(0, 100);
                solution.addFirst(randVal);
                solution.addFirst(randVal);
                System.out.println("addFirst:(" + randVal + ")");
            } else if (operationNumber == 1) {
                int size = solution.size();
                System.out.println("size:(" + size + ")");
            } else if (operationNumber == 2) {
                int last = solution.getLast();
                if (solution.size() >= 0) {
                    assertEquals("No equal", solution.removeLast(), studuent.removeLast());
                    System.out.println("removelast:" + last + ")");
                } else {
                    System.out.println("No enough size");
                }
            }
        }
    }
}
