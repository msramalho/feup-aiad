package labyrinth.agents.maze;

import labyrinth.maze.Directions;
import labyrinth.maze.Maze;
import labyrinth.utils.NegotiationEnvelope;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the internal confidences an Agent has over the maze
 * Including certainty values (beliefs) for the value of each cell
 */

public class MazeKnowledge {
    //TODO: be able to classify cell + direction as dead end.
    public enum CELL_STATE {MISTERY, WALL, PATH, END;}

    public int lines, columns;

    /**
     * Reflects the confidence in each state that the agent has over a given cell
     * Total trust in WALL is {Wall: 1.0}
     */
    public class CellConfidence {
        //TODO: maybe implement functions to combine certainties and such
        HashMap<CELL_STATE, Float> confidence = new HashMap<>();

        public CellConfidence(CELL_STATE state, Float conf) { confidence.put(state, conf);}


        /**
         * @return the CELL_STATE in which the agent trusts the most
         */
        public CELL_STATE getValue() {
            return confidence.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
        }

        /**
         * Check if there is still no information about this cell
         *
         * @return true if no info is stored
         */
        public boolean isUnknown() {
            return getValue() == CELL_STATE.MISTERY;
        }
    }

    public CellConfidence[][] confidences;

    public MazeKnowledge(Maze maze) {
        lines = maze.size.x;
        columns = maze.size.y;
        confidences = new CellConfidence[maze.size.x][maze.size.y];
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < columns; j++) {
                confidences[i][j] = new CellConfidence(CELL_STATE.MISTERY, (float) 1);
            }
        }
    }

    /**
     * Update the confidences of a given cell
     *
     * @param x          line
     * @param y          col
     * @param state      {@link CELL_STATE}
     * @param confidence confidence level [0,1]
     */
    public void update(int x, int y, CELL_STATE state, Float confidence) {
        confidences[x][y] = new CellConfidence(state, confidence);
    }

    public void update(int x, int y, CELL_STATE state) { update(x, y, state, 1F);}

    public void update(Directions d, CELL_STATE state, Float confidence) {
        update(d.direction.x, d.direction.y, state, confidence);
    }

    public void update(Directions d, CELL_STATE state) {
        update(d.direction.x, d.direction.y, state, 1F);
    }

    public void update(NegotiationEnvelope envelope) {
        //TODO: make the update on the current knoledge
    }

    /**
     * calculates the utiity of an envelope to a given agent
     * essentially, the ratio of new information over given information
     *
     * @param envelope containing the new info to assess
     * @return the ratio between 0 (useless) and 1 (very usefull)
     */
    public float getUtility(NegotiationEnvelope envelope) {
        return envelope.proposal.size() == 0 ? 0 : envelope.proposal.entrySet().stream()
                .filter(c -> confidences[c.getKey().l][c.getKey().r].isUnknown())
                .count() / envelope.proposal.size();
    }


    public void merge(MazeKnowledge newInfo) {
        //TODO: usefull for agents to share and update their internal confidences
    }

}
