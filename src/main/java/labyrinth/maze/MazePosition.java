package labyrinth.maze;

import labyrinth.utils.Vector2D;

public class MazePosition {


    private Vector2D position;
    private Maze maze;

    public MazePosition(Vector2D starterPosition, Maze maze) {

        this.position = starterPosition;
        this.maze = maze;
    }

    public Vector2D getPosition() {
        return position;
    }

    public boolean move(Directions direction) {
        Vector2D newPos = position.translate(direction.direction);
        if (maze.hasWallAt(newPos)) {
            return false;
        }

        position = newPos;
        return true;
    }

    public boolean atExit() {
        return position.equals(maze.endPos);
    }
}
