package labyrinth.agents;

import labyrinth.maze.Directions;
import labyrinth.maze.Maze;
import labyrinth.maze.MazePosition;
import sajas.core.Agent;

public class DumbAgent extends AwareAgent {

    public DumbAgent(MazePosition position, Maze maze) {
        super(position, maze);
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
