package labyrinth.maze;


import java.util.Random;

public class MazeFactory {

    private static final int DEFAULT_MAZE_X = 20;
    private static final int DEFAULT_MAZE_Y = 20;

    private int mazeXSize = DEFAULT_MAZE_X;
    private int mazeYSize = DEFAULT_MAZE_Y;

    public MazeFactory() {

    }

    public Maze buildMaze() {


        boolean[][] walls = buildRandomWalls(0.2, mazeXSize - 2, mazeYSize - 2);

        boolean[][] newWalls = wrapInWalls(walls, mazeXSize, mazeYSize);
        return new Maze(newWalls, mazeXSize, mazeYSize);
    }

    /**
     * Adds edges to the walls filled with walls
     * @param innerWalls
     * @param newXSize
     * @param newYSize
     * @return
     */
    private static boolean[][] wrapInWalls(boolean[][] innerWalls, int newXSize, int newYSize) {
        boolean[][] newWalls = new boolean[newXSize][newYSize];

        for (int i = 0; i < newXSize; i++) {
            newWalls[i][0] = true;
            newWalls[i][newYSize - 1] = true;
        }

        for (int j = 0; j < newYSize; j++) {
            newWalls[0][j] = true;
            newWalls[newXSize - 1][j] = true;
        }

        for (int i = 0; i < newXSize - 2; i++) {
            for (int j = 0; j < newYSize - 2; j++) {
                newWalls[i + 1][j + 1] = innerWalls[i][j];
            }
        }


        return newWalls;
    }

    /**
     * Purely random wall placement
     *
     * @return
     */
    private static boolean[][] buildRandomWalls(double generationRate, int mazeXSize, int mazeYSize) {
        Random r = new Random();

        boolean mazeWalls[][] = new boolean[mazeXSize][mazeYSize];

        for (int i = 0; i < mazeXSize; i++) {
            for (int j = 0; j < mazeYSize; j++) {
                mazeWalls[i][j] = r.nextDouble() < generationRate;
            }
        }

        return mazeWalls;
    }

}
