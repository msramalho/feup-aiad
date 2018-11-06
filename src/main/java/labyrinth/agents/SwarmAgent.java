package labyrinth.agents;

import labyrinth.agents.maze.knowledge.MazeKnowledge;
import labyrinth.agents.maze.MazePosition;

public class SwarmAgent extends BacktrackAgent {
    private static MazeKnowledge swarmKnowledgeSingleton = null;


    public SwarmAgent(MazePosition mazePosition, MazeKnowledge knowledge) {
        super(mazePosition, getSwarmKnowledge(knowledge));
    }

    private static MazeKnowledge getSwarmKnowledge(MazeKnowledge currentKnowledge) {
        if (swarmKnowledgeSingleton == null) {
            swarmKnowledgeSingleton = currentKnowledge;
        }
        return swarmKnowledgeSingleton;
    }
}
