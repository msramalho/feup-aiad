package labyrinth.maze.generators;

import labyrinth.maze.Directions;
import labyrinth.utils.Segment;
import labyrinth.utils.Vector2D;

import java.util.*;

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

    private void iterative(Vector2D init_pos) {
        Stack<Vector2D> stack = new Stack<>();
        stack.add(init_pos);

        while (!stack.isEmpty()) {
            Vector2D pos = stack.peek();

            List<Directions> directions = Directions.getRandomDirections();
            boolean added = false;
            for (Directions dir : directions) {
                Vector2D nextPos = pos.translate(dir.direction);
                if (nextPos.isInsideBounds(pathSize) && !stack.contains(nextPos) && !visitedCells[nextPos.x][nextPos.y]) {
                    stack.push(nextPos);
                    added = true;
                    break;
                }
            }
            if(added)
                edges.add(new Segment(pos, stack.peek()));
            else {
                visitedCells[pos.x][pos.y] = true;
                stack.pop();
            }
        }
    }

    @Override
    public boolean[][] generate() {
        iterative(Vector2D.ORIGIN);

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
