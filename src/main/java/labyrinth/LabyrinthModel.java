package labyrinth;

import labyrinth.display.MazeSpace;
import jade.core.Profile;
import jade.core.ProfileImpl;
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

    private MazeSpace mazeSpace;

    public LabyrinthModel() {
        super();
    }

    @Override
    public String getName() {
        return "AIAD - Labyrinth";
    }

    @Override
    public void begin() {
        System.out.println("===================BEGIN");

        super.begin();

        DisplaySurface displaySurf = new DisplaySurface(this, "Labyrinth Model");
        registerDisplaySurface("Labyrinth Model", displaySurf);

        try {
            MazeFactory mazeFactory = new MazeFactory(mazeSize);
            mazeSpace = new MazeSpace(mazeFactory.buildMaze());
        } catch (IOException e) {
            throw new IllegalStateException("Missing image files");
        }
        mazeSpace.addDisplayables(displaySurf);

        displaySurf.display();
//        getSchedule().scheduleActionAtInterval(1, )
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

    @Override
    protected void launchJADE() {
        System.out.println("===================JADE");
        // don't know the purpose of this
        Runtime rt = Runtime.instance();
        Profile p1 = new ProfileImpl();
        ContainerController mainContainer = rt.createMainContainer(p1);
        launchAgents(mainContainer);
    }

    private void launchAgents(ContainerController mainContainer) {

    }

    public static void main(String[] args) {
        SimInit init = new SimInit();
        LabyrinthModel model = new LabyrinthModel();
        init.loadModel(model, null, false);
    }
}
