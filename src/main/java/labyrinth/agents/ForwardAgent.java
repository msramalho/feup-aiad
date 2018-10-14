package labyrinth.agents;

import labyrinth.maze.Directions;
import labyrinth.maze.Maze;
import labyrinth.maze.MazePosition;

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
        if (position.atExit()) {
            return;
        }

        ArrayList<Directions> directions = position.getAvailableDirections();

        if(lastDirection != null && directions.size() == 2 && directions.contains(lastDirection)) {
            position.move(lastDirection);
        } else if (directions.size() >= 2) {
            directions.forEach(direction -> {
                if (direction != Directions.getOpposite(lastDirection)) {
                    position.move(direction);
                    lastDirection = direction;
                }
            });
        } else {
            position.move(directions.get(0));
            lastDirection = directions.get(0);
        }
    }
}
