package labyrinth;

import jade.wrapper.StaleProxyException;
import labyrinth.agents.*;
import labyrinth.statistics.AgentDescription;
import labyrinth.maze.Maze;
import labyrinth.agents.maze.knowledge.MazeKnowledge;
import labyrinth.agents.maze.MazePosition;
import labyrinth.statistics.AgentMetrics;
import labyrinth.utils.Pair;
import labyrinth.utils.Vector2D;
import sajas.wrapper.ContainerController;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class AgentBuilder {

    private final List<Pair<Color, Supplier<Vector2D>>> agentGraphics = new ArrayList<>();
    private final List<Runnable> agentTickRunners = new ArrayList<>();
    private final ContainerController mainContainer;
    private final Maze maze;
    private final List<Vector2D> startPositions;
    private int agentCounter = 0;
    private List<AgentDescription> agentDescriptions = new ArrayList<>();
    private HashMap<String, IAwareAgent> allAgents = new HashMap<>();

    public AgentBuilder(ContainerController mainContainer, Maze maze) {
        this.mainContainer = mainContainer;
        this.maze = maze;
        this.startPositions = maze.getStartPositions();
    }

    private void addAgent(Color agentColor, BiFunction<MazePosition, MazeKnowledge, AwareAgent> agentBuilder) throws StaleProxyException {
        Vector2D startPos = getStartIndex();

        MazePosition mazePosition = new MazePosition(startPos, maze);
        MazeKnowledge knowledge = new MazeKnowledge(maze);
        AgentMetrics agentMetrics = new AgentMetrics();

        AwareAgent agent = agentBuilder.apply(mazePosition, knowledge);

        agentGraphics.add(new Pair<>(agentColor, mazePosition::getPosition));
        agentTickRunners.add(agent::tick);
        String agentName = "agent " + agent.getClass().getSimpleName() + " #" + agentCounter;
        mainContainer.acceptNewAgent(agentName, agent).start();
        allAgents.put(agent.getAID().getName(), agent);

        agentDescriptions.add(new AgentDescription(mazePosition, agent, agentName));
    }

    private Vector2D getStartIndex() {
        final int startIndex = agentCounter % startPositions.size();
        Vector2D startPos = startPositions.get(startIndex);
        agentCounter++;
        return startPos;
    }

    public AgentBuilder addForwardAgent() throws StaleProxyException {
        addAgent(Color.pink, (mazePos, knowledge) -> new ForwardAgent(mazePos, knowledge));
        return this;
    }

    public AgentBuilder addBacktrackAgent() throws StaleProxyException {
        //orange
        addAgent(Color.decode("#FF8C00"), (mazePos, knowledge) -> new BacktrackAgent(mazePos, knowledge));
        return this;
    }

    public AgentBuilder addRandomAgent() throws StaleProxyException {
        addAgent(Color.cyan, (mazePos, knowledge) -> new RandomAgent(mazePos, knowledge));
        return this;
    }

    public AgentBuilder addNegotiatingAgent() throws StaleProxyException {
        addAgent(Color.red, (mazePos, knowledge) -> new NegotiatingAgent(mazePos, knowledge));
        return this;
    }

    public AgentBuilder addSwarmAgent() throws StaleProxyException {
        addAgent(Color.yellow, (mazePos, knowledge) -> new SwarmAgent(mazePos, knowledge));
        return this;
    }

    public List<Pair<Color, Supplier<Vector2D>>> buildAgentGraphics() {
        return this.agentGraphics;
    }

    public List<Runnable> buildAgentTickRunners() {
        return agentTickRunners;
    }

    public List<AgentDescription> getAgentsDescriptions() {
        return new ArrayList<>(agentDescriptions);
    }

    public Map<String, IAwareAgent> getAgents() {
        return new HashMap<>(allAgents);
    }

}
