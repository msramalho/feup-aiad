package labyrinth.agents;

import jade.lang.acl.ACLMessage;
import labyrinth.maze.Directions;
import labyrinth.maze.MazeKnowledge;
import labyrinth.maze.MazePosition;
import labyrinth.utils.Vector2D;
import sajas.core.Agent;
import sajas.core.behaviours.CyclicBehaviour;


/**
 * Maze Agent with some general useful skills, such as:
 * Easy interface for storing and updating maze Knowledge confidences
 * Easily receive and use messages
 */
public abstract class AwareAgent extends Agent {
    MazePosition mazePosition;
    MazeKnowledge knowledge;

    AwareAgent(MazePosition mazePosition, MazeKnowledge knowledge) {
        this.mazePosition = mazePosition;
        this.knowledge = knowledge;
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
     * Calculate the result of moving from current pos in dir direction
     * @param dir the direction of the move
     * @return the new pos
     */
    protected Vector2D getPosAfterMove(Directions dir){
        return mazePosition.getPosition().translate(dir.direction);
    }

    /**
     * Inheriting classes have a message to directly process received messages
     *
     * @param msg not null ACLMessage
     */
    protected void receiveMessage(ACLMessage msg) {}

    /**
     * Mandatory method to update agents
     */
    public abstract void tick();
}
