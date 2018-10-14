package labyrinth.agents;

import labyrinth.maze.Directions;
import labyrinth.maze.Maze;
import labyrinth.maze.MazePosition;

import java.awt.*;

public class DumbAgent extends AwareAgent {

    public DumbAgent(MazePosition position, Maze maze) {
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

    @Override
    protected Color getAgentColor() { return Color.blue; }
}
