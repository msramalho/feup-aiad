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
    private long stepsToExit = 0;
    private Maze maze;
    private Class<? extends AwareAgent> agentClazz;
    private long numDeadends = 0;
    private long numUpSteps;
    private long numLeftSteps;
    private long numRightSteps;
    private long numDownSteps;
    private Map<Class<?>, Integer> agentEncounters = new HashMap<>();

    private static final List<Class<? extends AwareAgent>> AGENT_TYPES = Arrays.asList(
            BacktrackAgent.class,
            ForwardAgent.class,
            NegotiatingAgent.class,
            RandomAgent.class,
            SwarmAgent.class
    );

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

    public void incrementDeadend() {
        numDeadends++;
    }

    public void incrementSteps() {
        stepsToExit++;
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
        if (! AGENT_TYPES.contains(clazz)) {
            throw new RuntimeException("Invalid agent type: " + clazz.getSimpleName() + " add it to the list" );
        }

        int count = agentEncounters.get(clazz);
        agentEncounters.put(clazz, count + 1);
    }

//    @JsonProperty
//    public long getStepsToExit() {
//        return stepsToExit;
//    }
//
//    @JsonProperty
//    public long getXSize() {
//        return maze.size.x;
//    }
//
//    @JsonProperty
//    public long getYSize() {
//        return maze.size.y;
//    }
//
//    @JsonProperty
//    public double getAverageSpeed() {
//        return stepsToExit / (double) getMazeArea();
//    }
//
//    @JsonProperty
//    public double getFreeWallRatio() {
//        return getNumMazeWalls() / (double) getMazeArea();
//    }
//
    @JsonProperty
    public String getAgentType() {
        return agentClazz.getSimpleName();
    }

    @JsonProperty
    public long getNumDeadends() {
        return numDeadends;
    }

    @JsonProperty
    public long getNumUpSteps() {
        return numUpSteps;
    }

    @JsonProperty
    public long getNumLeftSteps() {
        return numLeftSteps;
    }


    @JsonProperty
    public long getNumRightSteps() {
        return numRightSteps;
    }

    @JsonProperty
    public long getNumDownSteps() {
        return numDownSteps;
    }
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

    public void setName(String agentName) {

        this.agentName = agentName;
    }
}
