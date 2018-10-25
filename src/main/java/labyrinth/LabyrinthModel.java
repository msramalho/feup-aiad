package labyrinth;

import jade.wrapper.StaleProxyException;
import labyrinth.agents.AwareAgent;
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
import java.util.ArrayList;
import java.util.List;

public class LabyrinthModel extends Repast3Launcher {
    private static final boolean BATCH_MODE = false;
    private Vector2D mazeSize = new Vector2D(20, 20);
    private int actionSlownessRate = 1; // lower is faster
    private long seed = 1;
    private boolean batchMode;
    private static AgentBuilder builder;

    public LabyrinthModel(boolean batchMode) {
        super();
        this.batchMode = batchMode;

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

    @Override
    public void begin() {
        super.begin();
        if (!batchMode) {
            //buildAndScheduleDisplay();
        }
    }

    private void build(ContainerController mainContainer) throws StaleProxyException, IOException {

        RandomSingleton.setSeed(seed);

        // maze
        Maze maze = new MazeFactory(mazeSize).buildRecursiveMaze();

        // agents
        builder = new AgentBuilder(mainContainer, maze);
        builder.addBacktrackAgent(true)
                .addForwardAgent(true)
                .addRandomAgent(true);

        // clock ticks
        ClockPublisher clockPublisher = new ClockPublisher()
                .subscribe(builder.buildAgentTickRunners());

        // graphics
        if (!batchMode) {
            DisplaySurface displaySurf = new DisplaySurface(this, "Labyrinth Model");
            registerDisplaySurface("Labyrinth Model", displaySurf);

            new MazeSpace().addDisplayables(maze, builder.buildAgentGraphics(), displaySurf);
            displaySurf.display();
            clockPublisher.subscribe(displaySurf::updateDisplay);
        }

        getSchedule().scheduleActionAtInterval(actionSlownessRate, clockPublisher);
    }

    public static void main(String[] args) {
        SimInit init = new SimInit();
        init.setNumRuns(1);   // works only in batch mode
        LabyrinthModel model = new LabyrinthModel(BATCH_MODE);
        init.loadModel(model, null, BATCH_MODE);

    }

    public static List<String> getNeigbours(AwareAgent agent) {
        ArrayList<String> res = new ArrayList<>();
        builder.allAgents.forEach((name, pos) ->{
            if (!name.equals(agent.getAID().getName())) {
                res.add(name);
            }
        });

        return res;
        // return builder.allAgents.entrySet()
        //         .stream().filter(entry -> !entry.getKey().equals(agent.getName()))
        //         .map(HashMap.Entry::getKey).collect(Collectors.toList());
    }
}
