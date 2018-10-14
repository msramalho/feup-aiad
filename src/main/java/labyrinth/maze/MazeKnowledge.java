package labyrinth.maze;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the internal confidences an Agent has over the maze
 * Including certainty values (beliefs) for the value of each cell
 */
public class MazeKnowledge {
    public enum CellState {SEEN, WALL, PATH, END;}

    /**
     * Reflects the confidence in each state that the agent has over a given cell
     * Total trust in WALL is {Wall: 1.0}
     */
    class CellConfidence {
        //TODO: maybe implement functions to combine certainties and such
        HashMap<CellState, Float> confidence = new HashMap<>();


        public CellConfidence(CellState state, Float conf) { confidence.put(state, conf);}

        /**
         * @return the CellState in which the agent trusts the most
         */
        public CellState getvalue() {
            return confidence.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
        }

        /**
         * Check if there is still no information about this cell
         *
         * @return true if no info is stored
         */
        public boolean isUnknown() {
            return confidence.size() == 0;
        }
    }

    public CellConfidence[][] confidences;

    public MazeKnowledge(Maze maze) {
        confidences = new CellConfidence[maze.size.x][maze.size.y];
    }

    /**
     * Update the confidences of a given cell
     *
     * @param x     line
     * @param y     col
     * @param state {@link CellState}
     * @param confidence     confidence level [0,1]
     */
    public void update(int x, int y, CellState state, Float confidence) {
        confidences[x][y] = new CellConfidence(state, confidence);
    }

    public void update(int x, int y, CellState state) { update(x, y, state, 1F);}

    public void update(Directions d, CellState state, Float confidence) {
        update(d.direction.x, d.direction.y, state, confidence);
    }

    public void update(Directions d, CellState state) {
        update(d.direction.x, d.direction.y, state, 1F);
    }

    public void merge(MazeKnowledge newInfo) {
        //TODO: usefull for agents to share and update their internal confidences
    }

}
