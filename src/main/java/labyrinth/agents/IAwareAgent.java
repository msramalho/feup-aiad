package labyrinth.agents;

import jade.lang.acl.ACLMessage;
import labyrinth.agents.maze.MazePosition;
import labyrinth.agents.maze.knowledge.MazeKnowledge;
import uchicago.src.sim.engine.SimModel;

public interface IAwareAgent {

    MazeKnowledge getKnowledge();

    ACLMessage createCFP();

    ACLMessage handleCFP(ACLMessage msg);

    ACLMessage handleProposal(ACLMessage msg);

    ACLMessage acceptedProposal(ACLMessage msg);

    void rejectedProposal(ACLMessage msg);

    void tick();

    void handleTick();

    long sendTimestamp(ACLMessage msg, boolean sendToNeigbours);

    void print(String message);

    SimModel getAID();

    MazePosition getMazePosition();

    int getVisibility();
}
