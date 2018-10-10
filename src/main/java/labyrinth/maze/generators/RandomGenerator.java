package labyrinth.maze.generators;

import labyrinth.utils.Vector2D;

import java.util.Random;

public class RandomGenerator implements IGenerator {

    private final double generationRate;
    private final Vector2D mazeSize;

    public RandomGenerator(double generationRate, Vector2D mazeSize) {

        this.generationRate = generationRate;
        this.mazeSize = mazeSize;
    }

    /**
     * Purely random wall placement. Might not be solvable.
     *
     * @return
     */
    public boolean[][] generate() {
        Random r = new Random();

        boolean mazeWalls[][] = new boolean[mazeSize.x][mazeSize.y];

        mazeSize.foreachForwardRange((i, j) -> mazeWalls[i][j] = r.nextDouble() < generationRate);

        return mazeWalls;
    }
}
