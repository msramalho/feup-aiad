package labyrinth.agents.maze;

import labyrinth.maze.Directions;
import labyrinth.maze.Maze;
import labyrinth.statistics.AgentMetrics;
import labyrinth.utils.Vector2D;

import java.util.*;

public class MazePosition {


    private Vector2D position;
    private Maze maze;
    private final AgentMetrics metrics;
    private long tickCounter = 0;
    private boolean exited = false;
    private Directions prevDirection = null;
    private Set<Vector2D> visitedCells = new HashSet<>();

    public MazePosition(Vector2D starterPosition, Maze maze, AgentMetrics metrics) {
        this.position = starterPosition;
        this.maze = maze;
        this.metrics = metrics;
        visitedCells.add(starterPosition);
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
            visitedCells.add(newPos);
            checkDirectionInversion(prevDirection, direction);
            checkForDeadEnd(newPos);
            metrics.incrementSteps();
            metrics.incrementDirection(direction);
            if (atExit()) {
                metrics.setNumVisitedCells(visitedCells.size());
                exited = true;
            }
        }

        prevDirection = direction;
        return true;
    }

    private void checkDirectionInversion(Directions prevDirection, Directions newDirection) {
        if (prevDirection == null || newDirection == null) {
            return;
        }

        if (prevDirection.direction.translate(newDirection.direction).equals(Vector2D.ORIGIN)) {
            metrics.incrementDirectionChange();
        }

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
