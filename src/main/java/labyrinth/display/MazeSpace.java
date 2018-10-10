package labyrinth.display;

import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.space.Object2DGrid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MazeSpace {
	private Object2DGrid doorGrid;
    private List<Wall> walls = new ArrayList<>();

	
	public MazeSpace(int xSize, int ySize){

		doorGrid = new Object2DGrid(xSize, ySize);

		buildWalls(xSize, ySize);
	}

    public void addDisplayables(DisplaySurface displaySurf) {

        Object2DDisplay displayDoors = new Object2DDisplay(doorGrid);
        displayDoors.setObjectList(walls);

        displaySurf.addDisplayable(displayDoors, "Doors");
    }

    private void buildWalls(int numLifts, int numFloors) {
        Random r = new Random();

        for (int i = 0; i < numLifts; i++) {
            for (int j = 0; j < numFloors; j++) {
                if (r.nextBoolean()) {
                    continue;
                }

                Wall wall = new Wall(i, j);
                walls.add(wall);
                doorGrid.putObjectAt(wall.getX(), wall.getY(), wall);
            }
        }
    }

}
