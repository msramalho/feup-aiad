package labyrinth.agents;

import labyrinth.agents.maze.MazeKnowledge;
import labyrinth.agents.maze.MazePosition;

public class SwarmAgent extends BacktrackAgent {
    static public MazeKnowledge swarmKnowledge = null;


    public SwarmAgent(MazePosition mazePosition, MazeKnowledge knowledge) {
        super(mazePosition, knowledge);
        if (swarmKnowledge == null) {
            swarmKnowledge = knowledge;
        }
    }

    @Override
    public MazeKnowledge getKnowledge() {
        return swarmKnowledge;
    }
}
