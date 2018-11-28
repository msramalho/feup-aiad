package labyrinth.agents;

import jade.wrapper.StaleProxyException;
import labyrinth.agents.implementations.*;
import labyrinth.agents.maze.MazePosition;
import labyrinth.agents.maze.knowledge.MazeKnowledge;
import labyrinth.maze.Maze;
import labyrinth.statistics.AgentDescription;
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
    private Map<String, AwareAgent> allAgents = new HashMap<>();
    private List<AgentMetrics> agentMetricsList = new ArrayList<>();


    public AgentBuilder(ContainerController mainContainer, Maze maze) {
        this.mainContainer = mainContainer;
        this.maze = maze;
        this.startPositions = maze.getStartPositions();
    }

    private void addAgent(Color agentColor, BiFunction<MazePosition, MazeKnowledge, AwareAgent> agentBuilder) throws StaleProxyException {
        AgentMetrics agentMetrics = new AgentMetrics()
                .setMaze(maze);
        agentMetricsList.add(agentMetrics);

        agentMetrics.setStartingPosition(getStartIndex());
        Vector2D startPos = buildStartPosition();


        MazePosition mazePosition = new MazePosition(startPos, maze, agentMetrics);
        MazeKnowledge knowledge = new MazeKnowledge(maze);
        AwareAgent agent = agentBuilder.apply(mazePosition, knowledge);
        agent.setMetrics(agentMetrics);

        agentGraphics.add(new Pair<>(agentColor, mazePosition::getPosition));
        agentTickRunners.add(agent::tick);
        String agentName = "agent " + agent.getClass().getSimpleName() + " #" + agentCounter;
        mainContainer.acceptNewAgent(agentName, agent).start();
        allAgents.put(agent.getAID().getName(), agent);

        agentMetrics.setName(agentName);

        agentDescriptions.add(new AgentDescription(mazePosition, agent, agentName));
    }


    private int getStartIndex() {
        return agentCounter % startPositions.size();
    }

    private Vector2D buildStartPosition() {
        final int startIndex = getStartIndex();
        Vector2D startPos = startPositions.get(startIndex);
        agentCounter++;
        return startPos;
    }

    public AgentBuilder addForwardAgent() throws StaleProxyException {
        addAgent(Color.pink, ForwardAgent::new);
        return this;
    }

    public AgentBuilder addBacktrackAgent() throws StaleProxyException {
        //orange
        addAgent(Color.decode("#FF8C00"), BacktrackAgent::new);
        return this;
    }

    public AgentBuilder addRandomAgent() throws StaleProxyException {
        addAgent(Color.cyan, RandomAgent::new);
        return this;
    }

    public AgentBuilder addNegotiatingAgent() throws StaleProxyException {
        addAgent(Color.red, NegotiatingAgent::new);
        return this;
    }

    public AgentBuilder addSwarmAgent() throws StaleProxyException {
        addAgent(Color.yellow, SwarmAgent::new);
        return this;
    }

    public List<Pair<Color, Supplier<Vector2D>>> buildAgentGraphics() {
        return this.agentGraphics;
    }

    public List<Runnable> buildAgentTickRunners() {
        return agentTickRunners;
    }

    public List<AgentDescription> buildAgentsDescriptions() {
        return new ArrayList<>(agentDescriptions);
    }

    public Map<String, AwareAgent> buildAgents() {
        return new HashMap<>(allAgents);
    }

    public List<AgentMetrics> buildAgentMetrics() {
        return new ArrayList<>(agentMetricsList);
    }

}
