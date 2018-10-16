package labyrinth.maze;

import labyrinth.utils.Vector2D;

import java.util.ArrayList;
import java.util.List;

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

}
