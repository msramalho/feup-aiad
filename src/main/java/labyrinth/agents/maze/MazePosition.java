package labyrinth.agents.maze;

import labyrinth.maze.Directions;
import labyrinth.maze.Maze;
import labyrinth.statistics.AgentMetrics;
import labyrinth.utils.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class MazePosition {


    private Vector2D position;
    private Maze maze;
    private final AgentMetrics metrics;
    private long tickCounter = 0;
    private boolean exited = false;

    public MazePosition(Vector2D starterPosition, Maze maze, AgentMetrics metrics) {

        this.position = starterPosition;
        this.maze = maze;
        this.metrics = metrics;
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

        // "starts" at 1
        if (! exited) {
            checkForDeadEnd(position);
            metrics.incrementSteps();
            metrics.incrementDirection(direction);
            if (atExit()) {
                exited = true;
            }
        }

        return true;
    }

    private void checkForDeadEnd(Vector2D position) {
        long numFreeCells = Directions.directions
                .stream()
                .filter(dir -> {
                    Vector2D neighbour = position.translate(dir.direction);
                    return !maze.hasWallAt(neighbour);
                }).count();

        // hit a dead end
        if (numFreeCells == 1) {
            metrics.incrementDeadend();
        }
    }

    public boolean atExit() {
        return position.equals(maze.endPos);
    }

    /**
     * Get all possible directions for the next move.
     *
     * @return ArrayList<Directions> with all possible moves
     */
    public ArrayList<Directions> getAvailableDirections(boolean random) {
        ArrayList<Directions> nextDirections = new ArrayList<>();

        List<Directions> possible;
        if (random) possible = Directions.getRandomDirections();
        else possible = Directions.directions;


        for (Directions direction : possible) {
            Vector2D newPos = position.translate(direction.direction);
            if (!maze.hasWallAt(newPos)) {
                nextDirections.add(direction);
            }
        }

        return nextDirections;
    }
}
