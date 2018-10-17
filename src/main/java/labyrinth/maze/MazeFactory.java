package labyrinth.maze;


import labyrinth.maze.generators.IGenerator;
import labyrinth.maze.generators.RandomGenerator;
import labyrinth.maze.generators.RecursiveBacktracking;
import labyrinth.utils.Vector2D;

import java.util.function.Function;

public class MazeFactory {
    private Vector2D mazeSize;

    public MazeFactory(Vector2D mazeSize) {
        this.mazeSize = mazeSize;
    }

    private Maze buildMaze(Function<Vector2D, IGenerator> generatorBuilder) {
        Vector2D innerSize = mazeSize.translate(-2, -2);
        boolean[][] walls = generatorBuilder.apply(innerSize)
                .generate();

        MazeBuilder builder = new MazeBuilder(walls, innerSize);
        return builder.addWrappingWalls()
                .addMazePositionsWith4Corners()
                .buildMaze();
    }

    public Maze buildRecursiveMaze() {
        return buildMaze(size -> new RecursiveBacktracking(size));
    }

    public Maze buildRandomMaze(double fillRate) {
        return buildMaze(size -> new RandomGenerator(fillRate, size));
    }
}
