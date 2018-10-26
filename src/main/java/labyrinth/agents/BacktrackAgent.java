package labyrinth.agents;

import labyrinth.maze.Directions;
import labyrinth.agents.maze.MazeKnowledge;
import labyrinth.agents.maze.MazePosition;
import labyrinth.utils.Vector2D;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class BacktrackAgent extends AwareAgent {

    protected Stack<Directions> backtrackStack = new Stack<>();

    public BacktrackAgent(MazePosition mazePosition, MazeKnowledge knowledge) {
        super(mazePosition, knowledge);
    }

    @Override
    public void handleTick() {
        position.move(getNextStep());
    }

    /**
     * Implements a backtracking iteration: move to new cells or backtrack if no new is available
     *
     * @return the next step
     */
    private Directions getNextStep() {
        ArrayList<Directions> directions = position.getAvailableDirections(true);

        for (Directions d : directions){
            Vector2D pos = getPosAfterMove(d);
            if (knowledge.confidences[pos.x][pos.y].isUnknown()) {
                backtrackStack.push(Directions.getOpposite(d)); // add this to backtrackable steps
                return d;
            }

        }
        return backtrackStack.pop();// no new option -> go back
    }
}
