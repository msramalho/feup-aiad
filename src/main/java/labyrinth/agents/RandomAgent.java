package labyrinth.agents;

import labyrinth.maze.Directions;
import labyrinth.agents.maze.MazeKnowledge;
import labyrinth.agents.maze.MazePosition;

public class RandomAgent extends AwareAgent {

    public RandomAgent(MazePosition position, MazeKnowledge knowledge) {
        super(position, knowledge);
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
