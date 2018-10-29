package labyrinth.utils;

public class Utilities {
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

    public interface ExceptionRunner<T extends Exception> {
        void run() throws T;
    }

    public static <T extends Exception> void rangeExecutors(int numIterations, ExceptionRunner<T> executor) throws T {
        for (int i = 0; i < numIterations; i++ ) {
            executor.run();
        }
    }
}
