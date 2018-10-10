package labyrinth.display;

import labyrinth.maze.Maze;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.space.Object2DGrid;

import java.util.ArrayList;
import java.util.List;

public class MazeSpace {
    Object2DDisplay displayWalls;
    private List<Wall> walls = new ArrayList<>();

    public MazeSpace(Maze maze) {
        buildWalls(maze);
    }

    public void addDisplayables(DisplaySurface displaySurf) {

        displaySurf.addDisplayable(displayWalls, "Walls");
    }

    private void buildWalls(Maze maze) {
        Object2DGrid wallGrid = new Object2DGrid(maze.xSize, maze.ySize);

        for (int i = 0; i < maze.xSize; i++) {
            for (int j = 0; j < maze.ySize; j++) {
                if (! maze.hasWallAt(i, j)) {
                    continue;
                }

                Wall wall = new Wall(i, j);
                walls.add(wall);
                wallGrid.putObjectAt(wall.getX(), wall.getY(), wall);
            }
        }

        displayWalls = new Object2DDisplay(wallGrid);
        displayWalls.setObjectList(walls);
    }

}
