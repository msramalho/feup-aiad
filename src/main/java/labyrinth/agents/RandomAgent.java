package labyrinth.agents;

import labyrinth.maze.Directions;
import labyrinth.maze.Maze;
import labyrinth.maze.MazePosition;

import java.awt.*;

public class RandomAgent extends AwareAgent {

    public RandomAgent(MazePosition position, Maze maze) {
        super(position, maze);
    }

    @Override
    public void tick() {
        if (mazePosition.atExit()) {
            return;
        }

        for (Directions direction : Directions.getRandomDirections()) {
            if (mazePosition.move(direction)) {
                return;
            }
        }
    }
}
