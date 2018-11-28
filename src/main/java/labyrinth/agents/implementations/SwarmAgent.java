package labyrinth.agents.implementations;

import labyrinth.agents.maze.knowledge.MazeKnowledge;
import labyrinth.agents.maze.MazePosition;
import labyrinth.maze.Directions;
import labyrinth.utils.Pair;
import labyrinth.utils.Vector2D;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static labyrinth.agents.maze.knowledge.MazeKnowledge.isDeadEndStatic;

public class SwarmAgent extends BacktrackAgent {
    private static ConcurrentHashMap<Pair<Integer, Integer>, Pair<ArrayList<Directions>, Integer>> deadEndsStatic = new ConcurrentHashMap<>();

    public SwarmAgent(MazePosition mazePosition, MazeKnowledge knowledge) {
        super(mazePosition, knowledge);
    }

    protected boolean isDeadEnd(Vector2D position, Directions d) {
        return isDeadEndStatic(position, d, deadEndsStatic);
    }

    protected void updateDeadEnds(Pair<Integer, Integer> coord, ArrayList<Directions> dirs, int countContinuousBacktracks) {
        MazeKnowledge.updateDeadEndsStatic(coord, dirs, countContinuousBacktracks, deadEndsStatic);
    }
}
