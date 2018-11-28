package labyrinth.agents.implementations;

import labyrinth.agents.AwareAgent;
import labyrinth.maze.Directions;
import labyrinth.agents.maze.knowledge.MazeKnowledge;
import labyrinth.agents.maze.MazePosition;
import labyrinth.utils.Pair;
import labyrinth.utils.Vector2D;

import java.util.ArrayList;
import java.util.Stack;

public class BacktrackAgent extends AwareAgent {

    protected Stack<Directions> backtrackStack = new Stack<>();
    protected int countContinuosBacktracks = 0;
    protected Directions lastMove;

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

        for (Directions d : position.getAvailableDirections(true)) {
            Vector2D pos = getPosAfterMove(d);

            // if this is not yet explored
            if (!this.getKnowledge().confidences[pos.x][pos.y] &&
                    !isDeadEnd(position.getPosition(), d)) {
                if (countContinuosBacktracks > 0) { //if there was backtrack onto this position
                    ArrayList<Directions> dirs = new ArrayList<>();
                    dirs.add(Directions.getOpposite(lastMove));
                    updateDeadEnds(new Pair<>(position.getPosition().x, position.getPosition().y), dirs, countContinuosBacktracks);
                    countContinuosBacktracks = 0;
                }
                backtrackStack.push(Directions.getOpposite(d)); // add this to backtrackable steps
                return d;
            }
        }

        countContinuosBacktracks++;
        return backtrackStack.pop();// no new option -> go back
    }

    protected boolean isDeadEnd(Vector2D position, Directions d) {
        return this.getKnowledge().isDeadEndStatic(position, d);
    }

    protected void updateDeadEnds(Pair<Integer, Integer> coord, ArrayList<Directions> dirs, int countContinuousBacktracks) {
        this.getKnowledge().updateDeadEnds(coord, dirs, countContinuousBacktracks);
    }
}
