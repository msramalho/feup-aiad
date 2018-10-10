package labyrinth;

import labyrinth.display.MazeSpace;
import jade.core.Profile;
import jade.core.ProfileImpl;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.ContainerController;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.gui.DisplaySurface;

public class LabyrinthModel extends Repast3Launcher {
    private static final int DEFAULT_MAZE_X = 10;
    private static final int DEFAULT_MAZE_Y = 10;

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
        super.begin();

        DisplaySurface displaySurf = new DisplaySurface(this, "Labyrinth Model");
        registerDisplaySurface("Labyrinth Model", displaySurf);

        mazeSpace = new MazeSpace(DEFAULT_MAZE_X, DEFAULT_MAZE_Y);
        mazeSpace.addDisplayables(displaySurf);

        displaySurf.display();
    }

    @Override
    public String[] getInitParam() {
        return new String[]{};
    }

    @Override
    protected void launchJADE() {
        // don't know the purpose of this
        Runtime rt = Runtime.instance();
        Profile p1 = new ProfileImpl();
        ContainerController mainContainer = rt.createMainContainer(p1);
    }

    public static void main(String[] args) {
        SimInit init = new SimInit();
        LabyrinthModel model = new LabyrinthModel();
        init.loadModel(model, null, false);
    }
}
