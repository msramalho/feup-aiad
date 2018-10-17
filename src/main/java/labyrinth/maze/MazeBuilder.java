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

    public MazeBuilder addMazePositionsWith4Corners() {
        Vector2D corners[] = new Vector2D[]{
                new Vector2D(0,0),
                new Vector2D(size.x - 1, 0),
                new Vector2D(0, size.y - 1),
                size.translate(-1, -1)
        };
        for (Vector2D corner: corners) {
            walkSpacesAroundPoint(corner, (pos) -> {
                addStartPos(pos);
                return false;
            });
        }

        if (getNumPositions() == 0) {
            throw new IllegalArgumentException("Bad maze generation");
        }

        Vector2D middlePoint = new Vector2D(size.x / 2, size.y / 2);
        walkSpacesAroundPoint(middlePoint, (pos) -> {
            setEndPos(pos);
            return false;
        });

        return this;
    }

    public MazeBuilder addWrappingWalls() {
        Vector2D newSize = size.translate(2, 2);
        boolean[][] newWalls = new boolean[newSize.x][newSize.y];

        for (int i = 0; i < newSize.x; i++) {
            newWalls[i][0] = true;
            newWalls[i][newSize.y - 1] = true;
        }

        for (int j = 0; j < newSize.y; j++) {
            newWalls[0][j] = true;
            newWalls[newSize.x - 1][j] = true;
        }

        size.foreachForwardRange((i, j) -> newWalls[i + 1][j + 1] = walls[i][j]);

        walls = newWalls;
        size = newSize;

        return this;
    }
}
