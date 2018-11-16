package labyrinth.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import labyrinth.maze.Maze;
import labyrinth.utils.HeapValue;

public class AgentMetrics {
    private long stepsToExit;
    private Maze maze;

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

    private long getMazeArea() {
        return maze.size.x * maze.size.y;
    }

    private long getNumMazeWalls() {
        HeapValue<Long> numWalls = new HeapValue<>(0l);
        maze.foreachWall(v -> numWalls.value++);
        return numWalls.value;
    }

    public void setStepsToExit(long tickCounter) {
        this.stepsToExit = tickCounter;
    }

    public AgentMetrics setMaze(Maze maze) {
        this.maze = maze;
        return this;
    }
}
