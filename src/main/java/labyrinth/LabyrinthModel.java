package labyrinth;

import jade.wrapper.StaleProxyException;
import labyrinth.utils.ClockPublisher;
import labyrinth.display.MazeSpace;
import jade.core.Profile;
import jade.core.ProfileImpl;
import labyrinth.maze.Maze;
import labyrinth.utils.RandomSingleton;
import labyrinth.utils.Vector2D;
import labyrinth.maze.MazeFactory;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.ContainerController;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.gui.DisplaySurface;

import java.io.IOException;

public class LabyrinthModel extends Repast3Launcher {
    private Vector2D mazeSize = new Vector2D(20, 20);
    private int actionSlownessRate = 1; // lower is faster
    private long seed = 1;

    public LabyrinthModel() {
        super();
    }

    @Override
    public String getName() {
        return "AIAD - Labyrinth";
    }

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



    @Override
    public String[] getInitParam() {
        return new String[]{"MazeHeight", "MazeLength", "SlownessRate", "Seed"};
    }

    /**
     * runs after begin()
     */
    @Override
    protected void launchJADE() {
        // don't know the purpose of this
        Runtime rt = Runtime.instance();
        Profile p1 = new ProfileImpl();
        ContainerController mainContainer = rt.createMainContainer(p1);
        try {
            build(mainContainer);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void build(ContainerController mainContainer) throws StaleProxyException, IOException {

        RandomSingleton.setSeed(seed);

        DisplaySurface displaySurf = new DisplaySurface(this, "Labyrinth Model");
        registerDisplaySurface("Labyrinth Model", displaySurf);

        // maze
        Maze maze = new MazeFactory(mazeSize).buildRecursiveMaze();

        // agents
        AgentBuilder builder = new AgentBuilder(mainContainer, maze);
        builder.addBacktrackAgent()
                .addForwardAgent()
                .addRandomAgent();

        // graphics
        new MazeSpace().addDisplayables(maze, builder.buildAgentGraphics(), displaySurf);
        displaySurf.display();

        // clock ticks
        ClockPublisher clockPublisher = new ClockPublisher()
                .subscribe(builder.buildAgentTickRunners())
                .subscribe(displaySurf::updateDisplay);
        getSchedule().scheduleActionAtInterval(actionSlownessRate, clockPublisher);
    }

    public static void main(String[] args) {
        SimInit init = new SimInit();
        LabyrinthModel model = new LabyrinthModel();
        init.loadModel(model, null, false);
    }
}
