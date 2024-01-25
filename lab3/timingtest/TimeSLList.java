package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        AList<Integer> Ns = new AList<>();
        for (int i = 0; i < 8; i++) {
            Ns.addLast((int) Math.pow(2, i) * 1000);
        }
        AList<Double> time = new AList<>();
        AList<Integer> opCount = new AList<>();

        for (int i = 0; i < Ns.size(); i++){
            int count = 0;
            SLList<Integer> test = new SLList<>();
            for (int j = 0; j < Ns.get(i) ;j++){
                test.addLast(j);
            }
            int M = 1000;
            Stopwatch sw = new Stopwatch();
            for (int z = 0; z < M; z++) {
                test.getLast();
                count++;
            }
            time.addLast(sw.elapsedTime());
            opCount.addLast(count);
        }
        printTimingTable(Ns, time, opCount);
    }

}
