package labyrinth.agents;

import labyrinth.maze.MazePosition;
import sajas.core.Agent;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

import java.util.function.Consumer;

public class DumbAgent extends Agent implements Drawable {

    private final MazePosition mazePosition;
    private final Consumer<SimGraphics> drawer;

    public DumbAgent(MazePosition mazePosition, Consumer<SimGraphics> drawer) {
        this.mazePosition = mazePosition;
        this.drawer = drawer;
    }

    @Override
    public void draw(SimGraphics simGraphics) {
        drawer.accept(simGraphics);
    }

    @Override
    public int getX() {
        return mazePosition.getPosition().x;
    }

    @Override
    public int getY() {
        return mazePosition.getPosition().y;
    }
}
