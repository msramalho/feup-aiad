package labyrinth.agents;

import labyrinth.maze.Directions;
import labyrinth.agents.maze.MazeKnowledge;
import labyrinth.agents.maze.MazePosition;

public class RandomAgent extends AwareAgent {

    public RandomAgent(MazePosition position, MazeKnowledge knowledge, boolean isGUID) {
        super(position, knowledge, isGUID);
    }

    @Override
    public void tick() {
        if (position.atExit()) {
            return;
        }

        for (Directions direction : Directions.getRandomDirections()) {
            if (position.move(direction)) {
                return;
            }
        }
    }
}
