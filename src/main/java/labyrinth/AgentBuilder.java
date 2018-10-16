package labyrinth;

import jade.wrapper.StaleProxyException;
import labyrinth.agents.AwareAgent;
import labyrinth.agents.BacktrackAgent;
import labyrinth.agents.ForwardAgent;
import labyrinth.maze.Maze;
import labyrinth.maze.MazePosition;
import labyrinth.utils.Pair;
import labyrinth.utils.Vector2D;
import sajas.wrapper.ContainerController;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class AgentBuilder {

    private final List<Pair<Color, Supplier<Vector2D>>> agentGraphics = new ArrayList<>();
    private final List<Runnable> agentTickRunners = new ArrayList<>();
    private final ContainerController mainContainer;
    private final Maze maze;
    private int agentCounter = 0;

    public AgentBuilder(ContainerController mainContainer, Maze maze) {

        this.mainContainer = mainContainer;
        this.maze = maze;
    }

    private void addAgent(Color agentColor, Function<MazePosition, AwareAgent> agentBuilder) throws StaleProxyException {
        MazePosition mazePosition = new MazePosition(maze.startPos, maze);
        agentGraphics.add(new Pair<>(agentColor, mazePosition::getPosition));
        AwareAgent agent = agentBuilder.apply(mazePosition);
        agentTickRunners.add(agent::tick);
        mainContainer.acceptNewAgent("agent #" + agentCounter, agent).start();
        agentCounter++;
    }

    public AgentBuilder addForwardAgent() throws StaleProxyException {
        addAgent(Color.orange, (mazePos) -> new ForwardAgent(mazePos, maze));

        return this;
    }

    public AgentBuilder addBacktrackAgent() throws StaleProxyException {
        addAgent(Color.yellow, (mazePos) -> new BacktrackAgent(mazePos, maze));

        return this;
    }

    public List<Pair<Color, Supplier<Vector2D>>> buildAgentGraphics() {
        return this.agentGraphics;
    }

    public List<Runnable> buildAgentTickRunners() {
        return agentTickRunners;
    }

}
