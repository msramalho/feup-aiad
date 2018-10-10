package labyrinth.maze;

public class Maze {

    private final boolean[][] mazeWalls;
    public final int xSize;
    public final int ySize;

    public Maze(boolean[][] mazeWalls, int xSize, int ySize) {

        this.mazeWalls = mazeWalls;
        this.xSize = xSize;
        this.ySize = ySize;
    }

    public boolean hasWallAt(int x, int y) {
        return mazeWalls[x][y];
    }

}
