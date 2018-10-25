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

    public ForwardAgent(MazePosition position, MazeKnowledge knowledge, boolean isGUID) {
        super(position, knowledge, isGUID);
    }


    @Override
    public void tick() {
        if (position.atExit()) {
            return;
        }

        ArrayList<Directions> directions = position.getAvailableDirections(true);

        if (lastDirection != null && directions.size() == 2 && directions.contains(lastDirection)) {
            position.move(lastDirection);
        } else if (directions.size() >= 2) {
            for (Directions direction : directions) {
                if (direction != Directions.getOpposite(lastDirection)) {
                    position.move(direction);
                    lastDirection = direction;
                    return;
                }
            }
        } else {
            position.move(directions.get(0));
            lastDirection = directions.get(0);
        }
    }
}
