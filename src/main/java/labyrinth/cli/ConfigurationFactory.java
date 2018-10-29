package labyrinth.cli;

import jade.wrapper.StaleProxyException;
import labyrinth.AgentBuilder;
import labyrinth.agents.AwareAgent;
import labyrinth.display.MazeSpace;
import labyrinth.maze.Maze;
import labyrinth.maze.MazeFactory;
import labyrinth.utils.ClockPublisher;
import labyrinth.utils.RandomSingleton;
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

    public int getMazeHeight() {
        return mazeSize.y;
    }

    public void setMazeHeight(int height) {
        this.mazeSize = new Vector2D(mazeSize.x, height);
    }

    public int getMazeLength() {
        return mazeSize.x;
    }

    public void setMazeLength(int length) {
        this.mazeSize = new Vector2D(length, mazeSize.y);
    }

    public int getSlownessRate() {
        return actionSlownessRate;
    }

    public void setSlownessRate(int clocks) {
        this.actionSlownessRate = clocks;
    }

    public long getSeed() {
        return this.seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public boolean getBatchMode() {
        return batchMode;
    }

    public void setBatchMode(boolean batchMode) {
        this.batchMode = batchMode;
    }

    public Map<String, AwareAgent> build(ContainerController mainContainer, DisplaySurface displaySurf, Schedule schedule) throws StaleProxyException, IOException {
        RandomSingleton.setSeed(seed);

        // maze
        Maze maze = new MazeFactory(mazeSize).buildRecursiveMaze();

        // agents
        AgentBuilder builder = new AgentBuilder(mainContainer, maze);
        builder.addNegotiatingAgent()
                .addNegotiatingAgent()
                .addNegotiatingAgent()
                .addNegotiatingAgent()
                .addNegotiatingAgent()
                .addNegotiatingAgent()
                .addNegotiatingAgent();


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
}
