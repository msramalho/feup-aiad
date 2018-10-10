package labyrinth.maze;

import java.util.function.BiConsumer;

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

    public void foreachWall(BiConsumer<Integer, Integer> operator) {
        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                if (! hasWallAt(i, j)) {
                    continue;
                }

                operator.accept(i, j);
            }
        }
    }

}
