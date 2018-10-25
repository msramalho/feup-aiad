package labyrinth;

import jade.wrapper.StaleProxyException;
import labyrinth.agents.AwareAgent;
import labyrinth.agents.BacktrackAgent;
import labyrinth.agents.ForwardAgent;
import labyrinth.agents.RandomAgent;
import labyrinth.maze.Maze;
import labyrinth.agents.maze.MazeKnowledge;
import labyrinth.agents.maze.MazePosition;
import labyrinth.utils.Pair;
import labyrinth.utils.Vector2D;
import sajas.wrapper.ContainerController;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class AgentBuilder {

    private final List<Pair<Color, Supplier<Vector2D>>> agentGraphics = new ArrayList<>();
    private final List<Runnable> agentTickRunners = new ArrayList<>();
    private final ContainerController mainContainer;
    private final Maze maze;
    private final List<Vector2D> startPositions;
    private int agentCounter = 0;
    public HashMap<String, MazePosition> allAgents = new HashMap<>();

    public AgentBuilder(ContainerController mainContainer, Maze maze) {
        this.mainContainer = mainContainer;
        this.maze = maze;
        this.startPositions = maze.getStartPositions();
    }

    private void addAgent(Color agentColor, BiFunction<MazePosition, MazeKnowledge, AwareAgent> agentBuilder) throws StaleProxyException {
        final int startIndex = agentCounter % startPositions.size();
        Vector2D startPos = startPositions.get(startIndex);

        MazePosition mazePosition = new MazePosition(startPos, maze);
        MazeKnowledge knowledge = new MazeKnowledge(maze);
        AwareAgent agent = agentBuilder.apply(mazePosition, knowledge);

        agentGraphics.add(new Pair<>(agentColor, mazePosition::getPosition));
        agentTickRunners.add(agent::tick);
        mainContainer.acceptNewAgent("agent #" + agentCounter, agent).start();
        allAgents.put(agent.getAID().getName(), mazePosition);
        agentCounter++;
    }

    public AgentBuilder addForwardAgent(boolean isGUID) throws StaleProxyException {
        addAgent(Color.pink, (mazePos, knowledge) -> new ForwardAgent(mazePos, knowledge, isGUID));
        return this;
    }

    public AgentBuilder addBacktrackAgent(boolean isGUID) throws StaleProxyException {
        addAgent(Color.orange, (mazePos, knowledge) -> new BacktrackAgent(mazePos, knowledge, isGUID));
        return this;
    }

    public AgentBuilder addRandomAgent(boolean isGUID) throws StaleProxyException {
        addAgent(Color.cyan, (mazePos, knowledge) -> new RandomAgent(mazePos, knowledge, isGUID));
        return this;
    }

    public List<Pair<Color, Supplier<Vector2D>>> buildAgentGraphics() {
        return this.agentGraphics;
    }

    public List<Runnable> buildAgentTickRunners() {
        return agentTickRunners;
    }

}
