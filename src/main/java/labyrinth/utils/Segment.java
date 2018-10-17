package labyrinth.utils;

import java.util.function.Consumer;
import java.util.function.Function;

public class Segment {
    public final Vector2D start;
    public final Vector2D end;

    public Segment(Vector2D start, Vector2D end) {

        if (start.equals(end)) {
            throw new IllegalArgumentException("Can't be a point");
        }

        if (start.x != end.x && start.y != end.y) {
            throw new IllegalArgumentException("Points can't be diagonal");
        }

        this.start = start;
        this.end = end;
    }


    public Segment multiply(int factor) {
        return new Segment(start.multiply(factor), end.multiply(factor));
    }

    /**
     * Walks the segment. Can be stopped by the caller's lambda.
     *
     * @param func
     * @return true when the walk is fully consumed, false when interrupted by user
     */
    public boolean walkSegmentInclusive(Function<Vector2D, Boolean> func) {
        Vector2D direction = getUnitaryDirection();

        for (Vector2D p = start; !p.equals(end); p = p.translate(direction)) {
            if (!func.apply(p)) {
                return false;
            }
        }

        return func.apply(end);
    }

    public void walkSegmentInclusive(Consumer<Vector2D> consumer) {
        walkSegmentInclusive((p) -> {
            consumer.accept(p);
            return true;
        });
    }

    public boolean isHorizontal() {
        return start.y == end.y;
    }

    public int getLength() {
        int delta = isHorizontal() ? start.x - end.x : start.y - end.y;
        return Math.abs(delta);
    }

    public Vector2D getUnitaryDirection() {
        return end.subtract(start)
                .divide(getLength());
    }

    public static boolean walkSegment(Vector2D start, Vector2D end, Function<Vector2D, Boolean> func) {
        return new Segment(start, end).walkSegmentInclusive(func);
    }
}
