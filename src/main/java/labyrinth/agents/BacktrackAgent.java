package labyrinth.agents;

import labyrinth.maze.Directions;
import labyrinth.maze.MazeKnowledge;
import labyrinth.maze.MazePosition;
import labyrinth.utils.Vector2D;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class BacktrackAgent extends AwareAgent {

    protected HashSet<Vector2D> seen = new HashSet<>();
    protected Stack<Directions> backtrackStack = new Stack<>();

    public BacktrackAgent(MazePosition position, MazeKnowledge knowledge) { super(position, knowledge); }

    @Override
    public void tick() {
        if (mazePosition.atExit()) return;
        seen.add(mazePosition.getPosition()); // mark this as seen
        mazePosition.move(getNextStep());
    }

    /**
     * Implements a backtracking iteration: move to new cells or backtrack if no new is available
     *
     * @return the next step
     */
    private Directions getNextStep() {
        ArrayList<Directions> directions = mazePosition.getAvailableDirections(true);

        for (Directions d : directions)
            if (!seen.contains(getPosAfterMove(d))) {
                backtrackStack.push(Directions.getOpposite(d)); // add this to backtrackable steps
                return d;
            }

        return backtrackStack.pop();// no new option -> go back
    }
}
