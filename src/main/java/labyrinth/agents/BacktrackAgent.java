package labyrinth.agents;

import labyrinth.maze.Directions;
import labyrinth.agents.maze.MazeKnowledge;
import labyrinth.agents.maze.MazePosition;
import labyrinth.utils.Pair;
import labyrinth.utils.Vector2D;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class BacktrackAgent extends AwareAgent {

    private Stack<Directions> backtrackStack = new Stack<>();
    private int countContinuosBacktracks = 0;
    private Directions lastMove;

    public BacktrackAgent(MazePosition mazePosition, MazeKnowledge knowledge) {
        super(mazePosition, knowledge);
    }

    @Override
    public void handleTick() {
        position.move(lastMove = getNextStep());
    }

    /**
     * Implements a backtracking iteration: move to new cells or backtrack if no new is available
     *
     * @return the next step
     */
    private Directions getNextStep() {
        ArrayList<Directions> directions = position.getAvailableDirections(true);

        for (Directions d : directions) {
            Vector2D pos = getPosAfterMove(d);
            if (knowledge.confidences[pos.x][pos.y].isUnknown()) { // if this is not yet explored
                if (countContinuosBacktracks > 0) { //if there was backtrack onto this position
                    ArrayList<Directions> dirs = new ArrayList<>();
                    dirs.add(Directions.getOpposite(lastMove));
                    knowledge.updateDeadEnds(new Pair<>(position.getPosition().x, position.getPosition().y), dirs, countContinuosBacktracks);
                    print("FOUND DEAD END AT " + position.getPosition().x + ", " + position.getPosition().y + " going " + Directions.getOpposite(lastMove).toString() + " of cost " + countContinuosBacktracks);
                    countContinuosBacktracks = 0;
                }
                backtrackStack.push(Directions.getOpposite(d)); // add this to backtrackable steps
                return d;
            }
        }
        countContinuosBacktracks++;
        if (backtrackStack.empty()) { // failed to find exit, so restart, might have been due to wrong info from other agents
            knowledge.init();
            return getNextStep();
        }
        return backtrackStack.pop();// no new option -> go back
    }
}
