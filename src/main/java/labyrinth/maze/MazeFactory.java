package labyrinth.maze;


import labyrinth.maze.generators.RecursiveBacktracking;
import labyrinth.utils.Vector2D;

public class MazeFactory {
    private Vector2D mazeSize;

    public MazeFactory(Vector2D mazeSize) {
        this.mazeSize = mazeSize;
    }

    public Maze buildRecursiveMaze() {

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
     * @param walls
     * @param mazeSize
     * @return
     */
    private Maze addMazePositions(boolean[][] walls, Vector2D mazeSize) {
        MazeBuilder builder = new MazeBuilder(walls, mazeSize);

        Vector2D corners[] = new Vector2D[]{
                new Vector2D(0,0),
                new Vector2D(mazeSize.x - 1, 0),
                new Vector2D(0, mazeSize.y - 1),
                mazeSize.translate(-1, -1)
        };
        for (Vector2D corner: corners) {
            builder.walkSpacesAroundPoint(corner, (pos) -> {
                builder.addStartPos(pos);
                return false;
            });
        }

        if (builder.getNumPositions() == 0) {
            throw new IllegalArgumentException("Bad maze generation");
        }

        Vector2D middlePoint = new Vector2D(mazeSize.x / 2, mazeSize.y / 2);
        builder.walkSpacesAroundPoint(middlePoint, (pos) -> {
            builder.setEndPos(pos);
            return false;
        });

        return builder.buildMaze();
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
