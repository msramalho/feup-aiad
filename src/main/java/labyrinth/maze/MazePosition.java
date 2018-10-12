package labyrinth.maze;

import labyrinth.utils.Vector2D;

import java.util.ArrayList;
import java.util.Collections;

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

    /**
     * Get all possible directions for the next move.
     * @return ArrayList<Directions> with all possible moves
     */
    public ArrayList<Directions> getNextDirections(){
        ArrayList<Directions> nextDirections = new ArrayList<>();

        for (Directions direction : Directions.getRandomDirections()) {
            Vector2D newPos = position.translate(direction.direction);
            if (!maze.hasWallAt(newPos)) {
                nextDirections.add(direction);
            }
        }

        return nextDirections;
    }
}
