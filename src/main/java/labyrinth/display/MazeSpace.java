package labyrinth.display;

import labyrinth.maze.Maze;
import labyrinth.utils.Pair;
import labyrinth.utils.Vector2D;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.space.Object2DGrid;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MazeSpace {
    private BufferedImage wallImage = ImageIO.read(new File("resources/wall.png"));
    private static final Color START_COLOR = Color.BLUE;
    private static final Color END_COLOR = Color.GREEN;

    public MazeSpace() throws IOException {

    }

    public void addDisplayables(Maze maze, List<Pair<Color, Supplier<Vector2D>>> agentSuppliers, DisplaySurface displaySurf) {
        Object2DDisplay mazeCells = buildMazeGraphics(maze);
        displaySurf.addDisplayable(mazeCells, "Maze Cells");

        Object2DDisplay agentDrawables = buildAgentGraphics(maze, agentSuppliers);
        displaySurf.addDisplayable(agentDrawables, "Agents");
    }

    private Object2DDisplay buildGraphics(Maze maze, Consumer<List<MazeObject>> consumer) {
        Object2DGrid grid = new Object2DGrid(maze.size.x, maze.size.y);
        List<MazeObject> cells = new ArrayList<>();

        consumer.accept(cells);

        Object2DDisplay displayables = new Object2DDisplay(grid);
        displayables.setObjectList(cells);

        return displayables;
    }

    private Object2DDisplay buildMazeGraphics(Maze maze) {

        return buildGraphics(maze, cells -> {
            maze.foreachWall(p -> {
                MazeObject wall = new MazeObject(() -> p, g -> g.drawImageToFit(wallImage));
                cells.add(wall);
            });

            MazeObject start = new MazeObject(() -> maze.startPos, g -> g.drawRoundRect(START_COLOR));
            cells.add(start);

            MazeObject end = new MazeObject(() -> maze.endPos, g -> g.drawRoundRect(END_COLOR));
            cells.add(end);
        });
    }

    private Object2DDisplay buildAgentGraphics(Maze maze, List<Pair<Color, Supplier<Vector2D>>> agentSuppliers) {

        return buildGraphics(maze, cells -> {
            for (Pair<Color, Supplier<Vector2D>> pair: agentSuppliers) {
                MazeObject agent = new MazeObject(pair.r, g -> g.drawRoundRect(pair.l));
                cells.add(agent);
            }
        });
    }

}
