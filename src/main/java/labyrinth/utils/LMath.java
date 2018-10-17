package labyrinth.utils;

public class LMath {
    public static int max(int ... numbers) {
        if (numbers.length <= 1) {
            throw new IllegalArgumentException();
        }

        int maxValue = Integer.MIN_VALUE;
        for (int num: numbers) {
            maxValue = java.lang.Math.max(maxValue, num);
        }

        return maxValue;
    }
}
