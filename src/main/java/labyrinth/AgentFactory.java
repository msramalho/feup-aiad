package labyrinth;

import labyrinth.agents.AwareAgent;
import labyrinth.agents.ForwardAgent;
import labyrinth.maze.Maze;
import labyrinth.maze.MazePosition;
import labyrinth.utils.Vector2D;
import sajas.wrapper.ContainerController;

import java.util.List;
import java.util.function.Supplier;

public class AgentFactory {

    private final ContainerController mainContainer;
    private final List<Supplier<Vector2D>> agentPositions;
    private final Maze maze;

    public AgentFactory(ContainerController mainContainer, List<Supplier<Vector2D>> agentPositions, Maze maze) {

        this.mainContainer = mainContainer;
        this.agentPositions = agentPositions;
        this.maze = maze;
    }

    public void createAgent() {
        MazePosition mazePosition1 = new MazePosition(maze.startPos, maze);
        agentPositions.add(mazePosition1::getPosition);
        AwareAgent agent = new ForwardAgent(mazePosition1, maze);
        mainContainer.acceptNewAgent("dumb agent", agent).start();
    }
}
