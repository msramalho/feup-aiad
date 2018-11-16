package labyrinth.agents.implementations;

import labyrinth.agents.AwareAgent;
import labyrinth.maze.Directions;
import labyrinth.agents.maze.knowledge.MazeKnowledge;
import labyrinth.agents.maze.MazePosition;

public class RandomAgent extends AwareAgent {

    public RandomAgent(MazePosition position, MazeKnowledge knowledge) {
        super(position, knowledge);
    }

    @Override
    public void handleTick() {
        for (Directions direction : Directions.getRandomDirections()) {
            if (position.move(direction)) return;
        }
    }
}
