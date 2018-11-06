package labyrinth.agents.maze.knowledge;

import labyrinth.maze.Directions;
import labyrinth.maze.Maze;
import labyrinth.utils.NegotiationEnvelope;
import labyrinth.utils.Pair;
import labyrinth.utils.Vector2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the internal confidences an Agent has over the maze
 * Including certainty values (beliefs) for the value of each cell
 */

public class MazeKnowledge {

    public int lines, columns;
    //marks crossings + directions that lead to dead ends.
    // Coordinate -> directions that lead to dead ends + size (cumulative number of cells in the dead end)
    public HashMap<Pair<Integer, Integer>, Pair<ArrayList<Directions>, Integer>> deadEnds = new HashMap<>();

    public CellConfidence[][] confidences;

    public MazeKnowledge(Maze maze) {
        lines = maze.size.x;
        columns = maze.size.y;
        confidences = new CellConfidence[maze.size.x][maze.size.y];
        init();
    }

    /**
     * Update the confidences of a given cell
     *
     * @param x          line
     * @param y          col
     * @param state      {@link CellState}
     * @param confidence confidence level [0,1]
     */
    public void update(int x, int y, CellState state, Float confidence) {
        confidences[x][y] = new CellConfidence(state, confidence);
    }

    public void update(int x, int y, CellState state) { update(x, y, state, 1F);}

    public void update(NegotiationEnvelope envelope) {
        for (Map.Entry<Pair<Integer, Integer>, Pair<ArrayList<Directions>, Integer>> de : envelope.proposal.entrySet())
            updateDeadEnds(de.getKey(), de.getValue().l, de.getValue().r);
    }

    /**
     * calculates the utiity of an envelope to a given agent
     * essentially, the ratio of new information over given information
     *
     * @param envelope containing the new info to assess
     * @return the ratio between 0 (useless) and 1 (very usefull)
     */
    /*public float getUtility(NegotiationEnvelope envelope) {
        return envelope.proposal.size() == 0 ? 0 : envelope.proposal.entrySet().stream()
                .filter(c -> confidences[c.getKey().l][c.getKey().r].isUnknown())
                .count() / envelope.proposal.size();
    }*/
    public void init() {
        for (int i = 0; i < lines; i++)
            for (int j = 0; j < columns; j++)
                confidences[i][j] = new CellConfidence(CellState.MISTERY, (float) 1);
    }


    public boolean isDeadEnd(Vector2D position, Directions d) {
        Pair<ArrayList<Directions>, Integer> potentialDeadEnd = deadEnds.get(new Pair<>(position.x, position.y));
        if (potentialDeadEnd == null) return false;
        return potentialDeadEnd.l.contains(d);
    }

    /**
     * put or update deadends given a position, a direction and the number
     * of steps wasted in that direction for that cell
     *
     * @param coord
     * @param dir
     * @param cost
     */
    public void updateDeadEnds(Pair<Integer, Integer> coord, ArrayList<Directions> dir, Integer cost) {
        // Pair<Integer, Integer> coord = new Pair<>(position.getPosition().x, position.getPosition().y);
        Pair<ArrayList<Directions>, Integer> de = deadEnds.get(coord);
        if (de == null) {
            deadEnds.put(coord, new Pair<>(dir, cost));
        } else {
            de.l.addAll(dir);
            deadEnds.put(coord, new Pair<>(de.l, de.r + cost));
        }
    }

}
