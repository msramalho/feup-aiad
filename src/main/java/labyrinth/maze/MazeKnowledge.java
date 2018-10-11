package labyrinth.maze;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the internal confidences an Agent has over the maze
 * Including certainty values (beliefs) for the value of each cell
 */
public class MazeKnowledge {
    public enum CellState {UNKOWN, WALL, PATH, END;}

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
     * @param f     confidence level [0,1]
     */
    public void update(int x, int y, CellState state, Float f) {
        confidences[x][y] = new CellConfidence(state, f);
    }

    public void update(int x, int y, CellState state) {update(x, y, state, 1F); }


    public void merge(MazeKnowledge newInfo) {
        //TODO: usefull for agents to share and update their internal confidences
    }

}
