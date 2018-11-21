package labyrinth.utils;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

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

    public static void trySetBoolean(Map<String, String> map, String key,  Consumer<Boolean> setter) {
        trySetValue(map, key, Boolean::parseBoolean, setter);
    }

    public static void trySetLong(Map<String, String> map, String key,  Consumer<Long> setter) {
        trySetValue(map, key, Long::parseLong, setter);
    }

    public interface ExceptionRunner<T extends Exception> {
        void run() throws T;
    }

    public static <T extends Exception> void rangeExecutors(int numIterations, ExceptionRunner<T> executor) throws T {
        for (int i = 0; i < numIterations; i++ ) {
            executor.run();
        }
    }

    private static <V> void trySetValue(Map<String, String> map, String key, Function<String, V> parser, Consumer<V> setter) {
        if (map.containsKey(key)) {
            V value = parser.apply(map.get(key));
            setter.accept(value);
        }
    }

    public static void trySetInt(Map<String, String> map, String key, Consumer<Integer> setter) {
        trySetValue(map, key, Integer::parseInt, setter);
    }

    public static void trySetString(Map<String, String> map, String key, Consumer<String> setter) {
        trySetValue(map, key, String::valueOf, setter);
    }

}
