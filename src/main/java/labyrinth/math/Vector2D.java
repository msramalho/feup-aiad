package labyrinth.math;

import java.util.function.BiConsumer;

/**
 * 2D point or vector.
 * Immutable
 */
public class Vector2D {

    public final int x;
    public final int y;

    public Vector2D(int x, int y) {

        this.x = x;
        this.y = y;
    }

    public Vector2D translate(int dx, int dy) {
        return new Vector2D(x + dx, y + dy);
    }

    /**
     * Iterates each point sequentailly, left to right, bottom to top: (0,0), (1,0), (2,0), ..., (2,1), (3,1),..., (size.x - 1, size.y - 1)
     *
     * @param consumer
     */
    public void foreachForwardRange(BiConsumer<Integer, Integer> consumer) {
        for (int j = 0; j < y; j++) {
            for (int i = 0; i < x; i++) {
                consumer.accept(i, j);
            }
        }
    }

    public boolean equals(Vector2D other) {
        return x == other.x && y == other.y;
    }

}
