package labyrinth.display;

import labyrinth.maze.Maze;
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

public class MazeSpace {
    private BufferedImage wallImage = ImageIO.read(new File("resources/wall.png"));
    private static final Color START_COLOR = Color.BLUE;
    private static final Color END_COLOR = Color.GREEN;

    Object2DDisplay displayables;

    public MazeSpace(Maze maze) throws IOException {

        buildMazeGraphics(maze);
    }

    public void addDisplayables(DisplaySurface displaySurf) {

        displaySurf.addDisplayable(displayables, "Maze Cells");
    }

    private void buildMazeGraphics(Maze maze) {
        List<Cell> cells = new ArrayList<>();

        Object2DGrid cellGrid = new Object2DGrid(maze.size.x, maze.size.y);

        maze.foreachWall((i, j) -> {
            Cell wall = new Cell(i, j, g -> g.drawImageToFit(wallImage));
            cells.add(wall);
            cellGrid.putObjectAt(i, j, wall);
        });

        Cell start = new Cell(maze.startPos.x, maze.startPos.y, g -> g.drawRoundRect(START_COLOR));
        cells.add(start);

        Cell end = new Cell(maze.endPos.x, maze.endPos.y, g -> g.drawRoundRect(END_COLOR));
        cells.add(end);

        displayables = new Object2DDisplay(cellGrid);
        displayables.setObjectList(cells);
    }

}
