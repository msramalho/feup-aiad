package labyrinth.maze;


import java.util.Random;

public class MazeFactory {

    private static final int DEFAULT_MAZE_X = 10;
    private static final int DEFAULT_MAZE_Y = 10;

    private int mazeXSize = DEFAULT_MAZE_X;
    private int mazeYSize = DEFAULT_MAZE_Y;

    public MazeFactory() {

    }

    public Maze buildMaze() {

        Random r = new Random();

        boolean mazeWalls[][] = new boolean[mazeXSize][mazeYSize];

        for (int i = 0; i < mazeXSize; i++) {
            for (int j = 0; j < mazeYSize; j++) {
                mazeWalls[i][j] = r.nextBoolean();
            }
        }

        return new Maze(mazeWalls, mazeXSize, mazeYSize);
    }

}
