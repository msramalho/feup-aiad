package labyrinth.agents;

import labyrinth.maze.Directions;
import labyrinth.maze.MazePosition;
import sajas.core.Agent;

public class DumbAgent extends Agent {

    private final MazePosition mazePosition;

    public DumbAgent(MazePosition mazePosition) {
        this.mazePosition = mazePosition;
    }

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
