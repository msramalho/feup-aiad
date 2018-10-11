package labyrinth.display;

import labyrinth.utils.Vector2D;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class MazeObject implements Drawable {

    private final Consumer<SimGraphics> drawer;
    private final Supplier<Vector2D> positionGetter;

    public MazeObject(Supplier<Vector2D> positionGetter, Consumer<SimGraphics> drawer) {
        this.positionGetter = positionGetter;
        this.drawer = drawer;
    }

    @Override
    public void draw(SimGraphics simGraphics) {
        drawer.accept(simGraphics);
    }

    @Override
    public int getX() {
        return positionGetter.get().x;
    }

    @Override
    public int getY() {
        return positionGetter.get().y;
    }
}
