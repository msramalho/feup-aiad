package labyrinth.maze.generators;

import labyrinth.math.Vector2D;

import java.util.Random;

public class RandomGenerator {

    private final double generationRate;

    public RandomGenerator(double generationRate) {

        this.generationRate = generationRate;
    }

    /**
     * Purely random wall placement. Might not be solvable.
     *
     * @return
     */
    public boolean[][] generate(Vector2D mazeSize) {
        Random r = new Random();

        boolean mazeWalls[][] = new boolean[mazeSize.x][mazeSize.y];

        mazeSize.foreachForwardRange((i, j) -> mazeWalls[i][j] = r.nextDouble() < generationRate);

        return mazeWalls;
    }
}
