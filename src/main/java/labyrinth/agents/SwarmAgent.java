package labyrinth.agents;

import labyrinth.agents.maze.MazeKnowledge;
import labyrinth.agents.maze.MazePosition;

public class SwarmAgent extends BacktrackAgent {
    private static MazeKnowledge swarmKnowledgeSingleton = null;


    public SwarmAgent(MazePosition mazePosition, MazeKnowledge knowledge) {
        super(mazePosition, calculateMazeKnowledge(knowledge));
    }

    private static MazeKnowledge calculateMazeKnowledge(MazeKnowledge currentKnowledge) {
        if (swarmKnowledgeSingleton == null) {
            swarmKnowledgeSingleton = currentKnowledge;
        }
        return swarmKnowledgeSingleton;
    }
}
