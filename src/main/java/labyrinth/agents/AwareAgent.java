package labyrinth.agents;

import jade.lang.acl.ACLMessage;
import labyrinth.maze.Maze;
import labyrinth.maze.MazeKnowledge;
import labyrinth.maze.MazePosition;
import sajas.core.Agent;
import sajas.core.behaviours.CyclicBehaviour;


/**
 * Maze Agent with some general useful skills, such as:
 * Easy interface for storing and updating maze Knowledge confidences
 * Easily receive and use messages
 */
public abstract class AwareAgent extends Agent {
    protected MazePosition position;
    protected MazeKnowledge knowledge;

    public AwareAgent(MazePosition position, Maze maze) {
        this.position = position;
        knowledge = new MazeKnowledge(maze);
    }

    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new MessageBehaviour());
    }

    //TODO: not sure if this is worth isolating in another file (possibly not, as there may be more behaviours)
    class MessageBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            ACLMessage msg = receive();
            if (msg != null) receiveMessage(msg);
            block();// block doesn't stop execution - just schedules the next execution
        }
    }

    /**
     * Inheriting classes have a message to directly process received messages
     *
     * @param msg not null ACLMessage
     */
    protected void receiveMessage(ACLMessage msg) {}
}
