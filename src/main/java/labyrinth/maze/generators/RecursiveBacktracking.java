package labyrinth.maze.generators;

import labyrinth.maze.Directions;
import labyrinth.utils.Segment;
import labyrinth.utils.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class RecursiveBacktracking implements IGenerator {

    private final Vector2D gridSize;
    private final Vector2D pathSize;

    private final boolean[][] visitedCells;
    private final List<Segment> edges;


    public RecursiveBacktracking(Vector2D gridSize) {
        this.gridSize = gridSize;
        this.pathSize = gridSize.translate(1, 1).divide(2);
        visitedCells = new boolean[gridSize.x][gridSize.y];
        edges = new ArrayList<>();
    }

    private boolean recursive(Vector2D pos) {
        if (! pos.isInsideGrid(pathSize) || visitedCells[pos.x][pos.y]) {
            return false;
        }

        visitedCells[pos.x][pos.y] = true;

        List<Directions> directions = Directions.getRandomDirections();
        for (Directions dir : directions) {
            Vector2D nextPos = pos.translate(dir.direction);
            if (recursive(nextPos)) {
                edges.add(new Segment(pos, nextPos));
            }
        }

        return true;
    }

    @Override
    public boolean[][] generate() {
        recursive(Vector2D.ORIGIN);

        return createWalls(edges, gridSize);
    }

    private boolean[][] createWalls(List<Segment> edges, Vector2D gridSize) {
        boolean grid[][] = new boolean[gridSize.x][gridSize.y];
        gridSize.foreachForwardRange((x,y)-> grid[x][y] = true);


        for (Segment edge: edges) {
            edge.multiply(2).walkSegmentInclusive(p -> {
                grid[p.x][p.y] = false;
            });
        }

        return grid;
    }
}
