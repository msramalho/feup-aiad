package labyrinth.agents;

import labyrinth.agents.maze.knowledge.MazeKnowledge;
import labyrinth.agents.maze.MazePosition;

public class SwarmAgent extends BacktrackAgent {
    private static MazeKnowledge swarmKnowledgeSingleton = null;


    public SwarmAgent(MazePosition mazePosition, MazeKnowledge knowledge) {
        super(mazePosition, getSwarmKnoweldge(knowledge));
    }

    private static MazeKnowledge getSwarmKnoweldge(MazeKnowledge currentKnowledge) {
        if (swarmKnowledgeSingleton == null) {
            swarmKnowledgeSingleton = currentKnowledge;
        }
        return swarmKnowledgeSingleton;
    }
}
