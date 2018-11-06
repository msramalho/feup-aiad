package labyrinth.agents.maze.knowledge;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Reflects the confidence in each state that the agent has over a given cell
 * Total trust in WALL is {Wall: 1.0}
 */
public class CellConfidence {
    //TODO: maybe implement functions to combine certainties and such
    Map<CellState, Float> confidence = new EnumMap<>(CellState.class);

    public CellConfidence(CellState state, Float conf) {
        confidence.put(state, conf);
    }


    /**
     * @return the CELL_STATE in which the agent trusts the most
     */
    public CellState getValue() {
        return confidence.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
    }

    /**
     * Check if there is still no information about this cell
     *
     * @return true if no info is stored
     */
    public boolean isUnknown() {
        return getValue() == CellState.MISTERY;
    }
}
