package labyrinth.maze;

import labyrinth.utils.LMath;
import labyrinth.utils.Segment;
import labyrinth.utils.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MazeBuilder {

    private List<Vector2D> startingPositions = new ArrayList<>();
    private boolean[][] walls;
    private Vector2D size;
    private Vector2D endPos;

    public MazeBuilder(boolean[][] walls, Vector2D size) {
        this.walls = walls;
        this.size = size;
    }

    public MazeBuilder setEndPos(Vector2D endPos) {
        this.endPos = endPos;
        return this;
    }

    public MazeBuilder addStartPos(Vector2D startPos) {
        startingPositions.add(startPos);
        return this;
    }

    public Maze buildMaze() {
        if (walls == null || size == null || endPos == null || startingPositions.size() == 0) {
            throw new IllegalArgumentException();
        }

        return new Maze(walls, size, startingPositions, endPos);
    }

    public int getNumPositions() {
        return startingPositions.size();
    }

    private boolean hasWallAt(Vector2D pos) {
        if (pos.x < 0 || pos.y < 0 || pos.x >= size.x || pos.y >= size.y) {
            return true;
        }
        return walls[pos.x][pos.y];
    }

    /**
     * Walks the empty spaces that expand from the point in a 'square circle'
     *
     * @param point
     * @param func
     */
    public boolean walkSpacesAroundPoint(Vector2D point, Function<Vector2D, Boolean> func) {
        if (!point.isInsideBounds(size)) {
            throw new IllegalArgumentException("Point out of bounds");
        }

        Function<Vector2D, Boolean> tryWalk = pos -> {
            if (hasWallAt(pos)) {
                return true;
            }
            return func.apply(pos);
        };

        if (! tryWalk.apply(point)) {
            return false;
        }

        final int highestDistance = LMath.max(point.x, point.y, size.x - point.x - 1, size.y - point.y - 1);

        for (int distance = 1; distance <= highestDistance; distance++) {
            Vector2D topLeft = point.translate(-distance, distance);
            Vector2D topRight = point.translate(distance, distance);
            Vector2D botLeft = point.translate(-distance, -distance);
            Vector2D botRight = point.translate(distance, -distance);

            if (!Segment.walkSegment(topLeft, topRight, tryWalk) ||
                    !Segment.walkSegment(topRight, botRight, tryWalk) ||
                    !Segment.walkSegment(botRight, botLeft, tryWalk) ||
                    !Segment.walkSegment(botLeft, topLeft, tryWalk)) {
                return false;
            }

        }

        return true;
    }
}
