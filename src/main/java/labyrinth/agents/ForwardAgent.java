package labyrinth.agents;

import labyrinth.maze.Directions;
import labyrinth.maze.Maze;
import labyrinth.maze.MazePosition;

import java.awt.*;
import java.util.ArrayList;

/**
 * Agent that will always follow the previous step.
 */
public class ForwardAgent extends AwareAgent {

    private Directions lastDirection;

    public ForwardAgent(MazePosition position, Maze maze) {
        super(position, maze);
    }

    @Override
    public void tick() {
        if (mazePosition.atExit()) {
            return;
        }

        ArrayList<Directions> directions = mazePosition.getAvailableDirections(false);

        if (lastDirection != null && directions.size() == 2 && directions.contains(lastDirection)) {
            mazePosition.move(lastDirection);
        } else if (directions.size() >= 2) {
            directions.forEach(direction -> {
                if (direction != Directions.getOpposite(lastDirection)) {
                    mazePosition.move(direction);
                    lastDirection = direction;
                }
            });
        } else {
            mazePosition.move(directions.get(0));
            lastDirection = directions.get(0);
        }
    }

    @Override
    protected Color getAgentColor() {return Color.pink; }
}
