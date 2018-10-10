package labyrinth.utils;

import labyrinth.maze.Maze;

import java.util.function.Consumer;

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

    public void walkSegmentInclusive(Consumer<Vector2D> consumer) {
        Vector2D direction = getUnitaryDirection();

        for (Vector2D p = start; !p.equals(end); p = p.translate(direction)) {
            consumer.accept(p);
        }

        consumer.accept(end);
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
}
