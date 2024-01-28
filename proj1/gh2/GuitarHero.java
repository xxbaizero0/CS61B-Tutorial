package gh2;
import deque.ArrayDeque;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.introcs.StdAudio;

public class GuitarHero {
    public static void main(String[] args) {
        final String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
        final int FREQ = 440;
        ArrayDeque<GuitarString> guitar = new ArrayDeque<>();
        int index = -1;
        while (true) {
            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int i = keyboard.indexOf(key);
                double freq = FREQ * Math.pow(2, (double) (i - 24) /12);
                guitar.addLast(new GuitarString(freq));
                if (guitar.size() > index) {
                    index++;
                }
                guitar.get(index).pluck();
                }
            double sample = 0.0;

            for (int j = 0; j < guitar.size(); j++) {
                sample += guitar.get(j).sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (int j = 0; j < guitar.size(); j++) {
                guitar.get(j).tic();
            }

        }
    }
}
