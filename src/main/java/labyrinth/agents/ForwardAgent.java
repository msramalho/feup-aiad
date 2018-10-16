package labyrinth.agents;

import labyrinth.maze.Directions;
import labyrinth.agents.maze.MazeKnowledge;
import labyrinth.agents.maze.MazePosition;

import java.util.ArrayList;

/**
 * Agent that will always follow the previous step.
 */
public class ForwardAgent extends AwareAgent {

    private Directions lastDirection;

    public ForwardAgent(MazePosition position, MazeKnowledge knowledge) {
        super(position, knowledge);
    }

    @Override
    public void tick() {
        if (mazePosition.atExit()) {
            return;
        }

        ArrayList<Directions> directions = mazePosition.getAvailableDirections(true);

        if (lastDirection != null && directions.size() == 2 && directions.contains(lastDirection)) {
            mazePosition.move(lastDirection);
        } else if (directions.size() >= 2) {
            for (Directions direction : directions) {
                if (direction != Directions.getOpposite(lastDirection)) {
                    mazePosition.move(direction);
                    lastDirection = direction;
                    return;
                }
            }
        } else {
            mazePosition.move(directions.get(0));
            lastDirection = directions.get(0);
        }
    }
}
