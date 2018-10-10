package labyrinth.maze;

import labyrinth.utils.Vector2D;

import java.util.function.BiConsumer;

public class Maze {

    public final Vector2D size;
    public final Vector2D startPos;
    public final Vector2D endPos;

    private final boolean[][] mazeWalls;


    public Maze(boolean[][] mazeWalls, Vector2D size, Vector2D startPos, Vector2D endPos) {
        this.mazeWalls = mazeWalls;
        this.size = size;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    public boolean hasWallAt(int x, int y) {
        return mazeWalls[x][y];
    }

    public void foreachWall(BiConsumer<Integer, Integer> consumer) {
        size.foreachForwardRange((i, j) -> {
            if (hasWallAt(i, j)) {
                consumer.accept(i, j);
            }
        });
    }

}
