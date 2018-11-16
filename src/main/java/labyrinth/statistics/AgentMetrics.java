package labyrinth.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import labyrinth.maze.Maze;
import labyrinth.utils.HeapValue;

public class AgentMetrics {
    private long stepsToExit = 0;
    private Maze maze;
    private String agentType;
    private long numDeadends = 0;

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

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }

    public void incrementDeadend() {
        numDeadends++;
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
    public String getAgentType() {
        return agentType;
    }

    @JsonProperty
    public long getNumDeadends() {
        return numDeadends;
    }

    public void incrementSteps() {
        stepsToExit++;
    }
}
