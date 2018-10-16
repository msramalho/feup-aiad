package labyrinth.maze;

import labyrinth.utils.Vector2D;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
        if (x < 0 || y < 0) {
            return true; // maybe throw exception
        }
        return mazeWalls[x][y];
    }

    public boolean hasWallAt(Vector2D newPos) {
        return hasWallAt(newPos.x, newPos.y);
    }


    public void foreachWall(Consumer<Vector2D> consumer) {
        size.foreachForwardRange((i, j) -> {
            if (hasWallAt(i, j)) {
                consumer.accept(new Vector2D(i, j));
            }
        });
    }

}
