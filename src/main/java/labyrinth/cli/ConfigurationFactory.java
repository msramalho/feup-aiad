package labyrinth.cli;

import com.fasterxml.jackson.annotation.JsonProperty;
import jade.wrapper.StaleProxyException;
import labyrinth.AgentBuilder;
import labyrinth.agents.AwareAgent;
import labyrinth.display.MazeSpace;
import labyrinth.maze.Maze;
import labyrinth.maze.MazeFactory;
import labyrinth.utils.ClockPublisher;
import labyrinth.utils.RandomSingleton;
import labyrinth.utils.Utilities;
import labyrinth.utils.Vector2D;
import sajas.wrapper.ContainerController;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.gui.DisplaySurface;

import java.io.IOException;
import java.util.Map;

public class ConfigurationFactory {
    private Vector2D mazeSize = new Vector2D(30, 30);
    private int actionSlownessRate = 1; // lower is faster
    private long seed = System.currentTimeMillis();
    private boolean batchMode = false;

    private int numForwardAgents = 1;
    private int numBacktrackAgents = 1;
    private int numRandomAgents = 1;
    private int numNegotiatingAgents = 1;

    public Map<String, AwareAgent> build(ContainerController mainContainer, DisplaySurface displaySurf, Schedule schedule) throws StaleProxyException, IOException {
        RandomSingleton.setSeed(seed);

        // maze
        Maze maze = new MazeFactory(mazeSize).buildRecursiveMaze();

        // agents
        AgentBuilder builder = new AgentBuilder(mainContainer, maze);
        Utilities.rangeExecutors(numBacktrackAgents, builder::addBacktrackAgent);
        Utilities.rangeExecutors(numForwardAgents, builder::addForwardAgent);
        Utilities.rangeExecutors(numRandomAgents, builder::addRandomAgent);
        Utilities.rangeExecutors(numNegotiatingAgents, builder::addNegotiatingAgent);


        // clock ticks
        ClockPublisher clockPublisher = new ClockPublisher()
                .subscribe(builder.buildAgentTickRunners());

        // graphics
        if (!batchMode) {
            new MazeSpace().addDisplayables(maze, builder.buildAgentGraphics(), displaySurf);
            displaySurf.display();
            clockPublisher.subscribe(displaySurf::updateDisplay);
        }

        // win checker
        WinChecker checker = new WinChecker(builder.getMazePositions(), batchMode);
        clockPublisher.subscribe(checker::tick);

        schedule.scheduleActionAtInterval(actionSlownessRate, clockPublisher);

        return builder.getAgents();

    }

    public int getMazeHeight() {
        return mazeSize.y;
    }

    public int getMazeLength() {
        return mazeSize.x;
    }

    public long getSeed() {
        return this.seed;
    }

    public boolean getBatchMode() {
        return batchMode;
    }

    public int getSlownessRate() {
        return actionSlownessRate;
    }

    @JsonProperty
    public void setMazeLength(int length) {
        this.mazeSize = new Vector2D(length, mazeSize.y);
    }

    @JsonProperty
    public void setSlownessRate(int clocks) {
        this.actionSlownessRate = clocks;
    }

    @JsonProperty
    public void setMazeHeight(int height) {
        this.mazeSize = new Vector2D(mazeSize.x, height);
    }

    @JsonProperty
    public void setSeed(long seed) {
        this.seed = seed;
    }

    @JsonProperty
    public void setBatchMode(boolean batchMode) {
        this.batchMode = batchMode;
    }


    public int getNumForwardAgents() {
        return numForwardAgents;
    }

    @JsonProperty
    public void setNumForwardAgents(int numForwardAgents) {
        this.numForwardAgents = numForwardAgents;
    }

    public int getNumBacktrackAgents() {
        return numBacktrackAgents;
    }

    @JsonProperty
    public void setNumBacktrackAgents(int numBacktrackAgents) {
        this.numBacktrackAgents = numBacktrackAgents;
    }

    public int getNumRandomAgents() {
        return numRandomAgents;
    }

    @JsonProperty
    public void setNumRandomAgents(int numRandomAgents) {
        this.numRandomAgents = numRandomAgents;
    }

    public int getNumNegotiatingAgents() {
        return numNegotiatingAgents;
    }

    @JsonProperty
    public void setNumNegotiatingAgents(int numNegotiatingAgents) {
        this.numNegotiatingAgents = numNegotiatingAgents;
    }
}
