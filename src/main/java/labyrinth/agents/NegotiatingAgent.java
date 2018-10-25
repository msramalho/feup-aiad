package labyrinth.agents;

import jade.lang.acl.ACLMessage;
import labyrinth.agents.maze.MazeKnowledge;
import labyrinth.agents.maze.MazePosition;

public class NegotiatingAgent extends AwareAgent {


    NegotiatingAgent(MazePosition position, MazeKnowledge knowledge, boolean isGUID) {
        super(position, knowledge, isGUID);
    }

    @Override
    protected void handleCFP(ACLMessage msg) {
        String content = msg.getContent();
        // msg.getContentObject();
    }

    @Override
    public void tick() {

    }
}
