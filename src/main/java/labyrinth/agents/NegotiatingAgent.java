package labyrinth.agents;

import jade.lang.acl.ACLMessage;
import labyrinth.agents.maze.MazeKnowledge;
import labyrinth.agents.maze.MazePosition;

public class NegotiatingAgent extends AwareAgent {


    NegotiatingAgent(MazePosition position, MazeKnowledge knowledge, boolean isGUID) {
        super(position, knowledge, isGUID);
    }


    @Override
    public void tick() {

    }
}
