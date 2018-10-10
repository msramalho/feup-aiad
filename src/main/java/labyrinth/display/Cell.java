package labyrinth.display;

import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

import java.util.function.Consumer;

public class Cell implements Drawable {

    private int x;
    private int y;
    private final Consumer<SimGraphics> drawer;

    public Cell(int x, int y, Consumer<SimGraphics> drawer) {
        this.x = x;
        this.y = y;

        this.drawer = drawer;
    }

    @Override
    public void draw(SimGraphics simGraphics) {
        drawer.accept(simGraphics);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
}
