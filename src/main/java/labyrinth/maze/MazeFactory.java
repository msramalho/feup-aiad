package labyrinth.maze;


import labyrinth.maze.generators.RecursiveBacktracking;
import labyrinth.utils.Vector2D;
import labyrinth.maze.generators.RandomGenerator;

public class MazeFactory {
    private Vector2D mazeSize;

    public MazeFactory(Vector2D mazeSize) {
        this.mazeSize = mazeSize;
    }

    public Maze buildMaze() {

        Vector2D innerWallsSize = mazeSize.translate(-2, -2);
        boolean[][] innerWalls = new RecursiveBacktracking(innerWallsSize)
                .generate();

        boolean[][] walls = wrapInWalls(innerWalls, mazeSize);
        return addMazePositions(walls, mazeSize);
    }

    private static final Vector2D DEFAULT_POS = new Vector2D(-1, -1);

    /**
     * Builds the maze and adds a starting and ending position at opposite ends
     *
     * @param newWalls
     * @param mazeSize
     * @return
     */
    private Maze addMazePositions(boolean[][] newWalls, Vector2D mazeSize) {
        Vector2D startPos = DEFAULT_POS;

        for (int j = mazeSize.y - 1; j >= 0; j--) {
            for (int i = 0; i < mazeSize.x; i++) {
                if (!newWalls[i][j]) {
                    startPos = new Vector2D(i, j);
                    break;
                }
            }
        }

        Vector2D endPos = DEFAULT_POS;
        for (int j = 0; j < mazeSize.y; j++) {
            for (int i = mazeSize.x - 1; i >= 0; i--) {
                if (!newWalls[i][j]) {
                    endPos = new Vector2D(i, j);
                    break;
                }
            }
        }

        if (startPos.equals(DEFAULT_POS) || endPos.equals(DEFAULT_POS) || startPos.equals(endPos)) {
            throw new IllegalArgumentException("Maze was build incorrectly, no starting or ending spots available");
        }

        return new Maze(newWalls, mazeSize, startPos, endPos);
    }

    /**
     * Adds edges to the walls filled with walls
     *
     * @param innerWalls
     * @return
     */
    private static boolean[][] wrapInWalls(boolean[][] innerWalls, Vector2D newSize) {
        boolean[][] newWalls = new boolean[newSize.x][newSize.y];

        for (int i = 0; i < newSize.x; i++) {
            newWalls[i][0] = true;
            newWalls[i][newSize.y - 1] = true;
        }

        for (int j = 0; j < newSize.y; j++) {
            newWalls[0][j] = true;
            newWalls[newSize.x - 1][j] = true;
        }

        newSize.translate(-2, -2)
                .foreachForwardRange((i, j) -> newWalls[i + 1][j + 1] = innerWalls[i][j]);

        return newWalls;
    }
}
