package labyrinth.agents.implementations;

import labyrinth.agents.AwareAgent;
import labyrinth.maze.Directions;
import labyrinth.agents.maze.knowledge.MazeKnowledge;
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
    public void handleTick() {
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