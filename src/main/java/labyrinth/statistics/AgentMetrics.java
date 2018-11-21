package labyrinth.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import labyrinth.agents.AwareAgent;
import labyrinth.agents.implementations.*;
import labyrinth.maze.Directions;
import labyrinth.maze.Maze;
import labyrinth.utils.HeapValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentMetrics {
    private static final List<Class<? extends AwareAgent>> AGENT_TYPES = Arrays.asList(
            BacktrackAgent.class,
            ForwardAgent.class,
            NegotiatingAgent.class,
            RandomAgent.class,
            SwarmAgent.class
    );

    private long stepsToExit;
    private Maze maze;
    private Class<? extends AwareAgent> agentClazz;
    private long numDeadends; // how many deadends found
    private long numUpSteps;
    private long numLeftSteps;
    private long numRightSteps;
    private long numDownSteps;
    private long numMessagesSent;
    private long numMessagesReceived;
    private long numCFPAnswered;
    private long numProposalsAccepted;
    private long numProposalsRejected;
    private long cumulatedUtilityReceived; // deadends negotiated with other agents
    private Map<Class<?>, Integer> agentEncounters = new HashMap<>();
    private int startIndex;
    private int startingPosition;

    private int numRandomAgents;
    private int numForwardAgents;
    private int numBacktrackAgents;
    private int numNegotiatingAgents;
    private int numSwarmAgents;
    private int numDirectionChanges = 0;
    private int numVisitedCells;

    public AgentMetrics() {
        AGENT_TYPES.forEach(name -> agentEncounters.put(name, 0));
    }

    private String agentName;

    private long getMazeArea() {
        return maze.size.x * maze.size.y;
    }

    private long getNumMazeWalls() {
        HeapValue<Long> numWalls = new HeapValue<>(0l);
        maze.foreachWall(v -> numWalls.value++);
        return numWalls.value;
    }

    public AgentMetrics setMaze(Maze maze) {
        this.maze = maze;
        return this;
    }

    public void setAgentClazz(Class<? extends AwareAgent> agentClazz) {
        this.agentClazz = agentClazz;
    }

    public void setName(String agentName) {
        this.agentName = agentName;
    }

    public void incrementDeadend() {
        numDeadends++;
    }

    public void incrementSteps() {
        stepsToExit++;
    }

    public void incrementMessagesSent() {
        numMessagesSent++;
    }

    public void incrementMessagesReceived() {
        numMessagesReceived++;
    }

    public void setStartingPosition(int startingPosition) {
        this.startingPosition = startingPosition;
    }

    public void incrementCFPAnswered() {
        numCFPAnswered++;
    }

    public void incrementProposalsAccepted() {
        numProposalsAccepted++;
    }

    public void incrementProposalsRejected() {
        numProposalsRejected++;
    }

    public void incrementCumulatedUtilityReceived(Integer u) {
        cumulatedUtilityReceived += u;
    }

    public void incrementDirection(Directions direction) {
        switch (direction) {
            case UP:
                numUpSteps++;
                break;
            case DOWN:
                numDownSteps++;
                break;
            case LEFT:
                numLeftSteps++;
                break;
            case RIGHT:
                numRightSteps++;
                break;
        }
    }

    public void incrementNeighbourEncounter(AwareAgent agent) {
        Class<? extends AwareAgent> clazz = agent.getClass();
        if (!AGENT_TYPES.contains(clazz)) {
            throw new RuntimeException("Invalid agent type: " + clazz.getSimpleName() + " add it to the list");
        }

        int count = agentEncounters.get(clazz);
        agentEncounters.put(clazz, count + 1);
    }

    public void setTotalAgentTypes(int numRandomAgents, int numForwardAgents, int numBacktrackAgents, int numNegotiatingAgents, int numSwarmAgents) {

        this.numRandomAgents = numRandomAgents;
        this.numForwardAgents = numForwardAgents;
        this.numBacktrackAgents = numBacktrackAgents;
        this.numNegotiatingAgents = numNegotiatingAgents;
        this.numSwarmAgents = numSwarmAgents;
    }

    @JsonProperty
    public long getStepsToExit() {
        return stepsToExit;
    }

    @JsonProperty
    public long getXSize() {
        return maze.size.x;
    }

    @JsonProperty
    public long getYSize() {
        return maze.size.y;
    }

    @JsonProperty
    public double getAverageSpeed() {
        return stepsToExit / (double) getMazeArea();
    }

    @JsonProperty
    public double getFreeWallRatio() {
        return getNumMazeWalls() / (double) getMazeArea();
    }

    @JsonProperty
    public String getAgentType() { return agentClazz.getSimpleName(); }

    @JsonProperty
    public long getNumDeadends() { return numDeadends; }

    @JsonProperty
    public long getNumUpSteps() { return numUpSteps; }

    @JsonProperty
    public long getNumLeftSteps() { return numLeftSteps; }

    @JsonProperty
    public long getNumRightSteps() { return numRightSteps; }

    @JsonProperty
    public long getNumDownSteps() {return numDownSteps; }

    @JsonProperty
    public long getNumMessagesSent() {return numMessagesSent;}

    @JsonProperty
    public long getNumMessagesReceived() {return numMessagesReceived;}

    @JsonProperty
    public long getNumCFPAnswered() {return numCFPAnswered;}

    @JsonProperty
    public long getNumProposalsAccepted() {return numProposalsAccepted;}

    @JsonProperty
    public long getNumProposalsRejected() {return numProposalsRejected;}

    @JsonProperty
    public long getCumulatedUtilityReceived() {return cumulatedUtilityReceived;}

    @JsonProperty
    public long getNumTotalEncounters() {
        return agentEncounters.values()
                .stream()
                .mapToInt(i -> i)
                .sum();
    }

    @JsonProperty
    public long getNumDifferentEncounters() {
        return agentEncounters.entrySet()
                .stream()
                .filter(entry -> !entry.getKey().equals(agentClazz))
                .mapToInt(entry -> entry.getValue())
                .sum();

    }

    @JsonProperty
    public String getAgentName() {
        return agentName;
    }

    @JsonProperty
    public int getStartingPosition() {
        return startingPosition;

    }

    @JsonProperty
    public int getNumRandomAgents() {
        return numRandomAgents;
    }

    @JsonProperty
    public int getNumForwardAgents() {
        return numForwardAgents;
    }

    @JsonProperty
    public int getNumBacktrackAgents() {
        return numBacktrackAgents;
    }

    @JsonProperty
    public int getNumNegotiatingAgents() {
        return numNegotiatingAgents;
    }

    @JsonProperty
    public int getNumSwarmAgents() {
        return numSwarmAgents;
    }

    public void incrementDirectionChange() {
        this.numDirectionChanges++;
    }

    @JsonProperty
    public int getNumDirectionChanges() {
        return numDirectionChanges;
    }

    public void setNumVisitedCells(int numVisitedCells) {
        this.numVisitedCells = numVisitedCells;
    }

    @JsonProperty
    public int getNumVisitedCells() {
        return numVisitedCells;
    }
}
