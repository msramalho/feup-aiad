package labyrinth;

import jade.wrapper.StaleProxyException;
import labyrinth.agents.ClockPublisher;
import labyrinth.agents.DumbAgent;
import labyrinth.display.MazeSpace;
import jade.core.Profile;
import jade.core.ProfileImpl;
import labyrinth.maze.Maze;
import labyrinth.agents.MazePosition;
import labyrinth.utils.Vector2D;
import labyrinth.maze.MazeFactory;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.ContainerController;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.gui.DisplaySurface;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class LabyrinthModel extends Repast3Launcher {
    private Vector2D mazeSize = new Vector2D(20, 20);

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

    @Override
    public String[] getInitParam() {
        return new String[]{"MazeHeight", "MazeLength"};
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

        DisplaySurface displaySurf = new DisplaySurface(this, "Labyrinth Model");
        registerDisplaySurface("Labyrinth Model", displaySurf);

        // maze
        Maze maze = new MazeFactory(mazeSize)
                .buildMaze();

        // agents
        MazePosition mazePosition = new MazePosition(maze.startPos, maze);
        DumbAgent agent = new DumbAgent(mazePosition);
        mainContainer.acceptNewAgent("dumb agent", agent).start();

        List<Supplier<Vector2D>> agentPositions = Arrays.asList(()-> mazePosition.getPosition());

        // graphics
        new MazeSpace()
                .addDisplayables(maze, agentPositions, displaySurf);
        displaySurf.display();

        // clock ticks
        ClockPublisher clockPublisher = new ClockPublisher();
        clockPublisher.subscribe(() -> agent.tick());
        clockPublisher.subscribe(() -> displaySurf.updateDisplay());
        getSchedule().scheduleActionAtInterval(1, clockPublisher);
    }

    public static void main(String[] args) {
        SimInit init = new SimInit();
        LabyrinthModel model = new LabyrinthModel();
        init.loadModel(model, null, false);
    }
}
