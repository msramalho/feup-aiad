package labyrinth.utils;

import java.util.Random;

public class RandomSingleton {
    private static long seed = 1;
    private static Random rand = null;


    public static Random instance() {
        if (rand == null) {
            rand = new Random(seed);
        }

        return rand;
    }

    public static void setSeed(long seedValue) {
        if (rand != null) {
            throw new IllegalArgumentException("Random has already been initialized");
        }
        seed = seedValue;
    }
}
