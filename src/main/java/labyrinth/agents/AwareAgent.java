package labyrinth.agents;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import labyrinth.LabyrinthModel;
import labyrinth.agents.behaviours.MessageBehaviour;
import labyrinth.agents.maze.knowledge.CellState;
import labyrinth.maze.Directions;
import labyrinth.agents.maze.knowledge.MazeKnowledge;
import labyrinth.agents.maze.MazePosition;
import labyrinth.utils.Vector2D;
import jade.core.AID;
import sajas.core.Agent;

import java.io.IOException;
import java.io.Serializable;


/**
 * Maze Agent with some general useful skills, such as:
 * Easy interface for storing and updating maze Knowledge confidences
 * Easily receive and use messages
 */
public abstract class AwareAgent extends Agent implements IAwareAgent {
    public MazePosition position;
    private MazeKnowledge knowledge;
    private MessageBehaviour messageBehaviour;

    // private Schedule sch;
    public int visibility = 1;
    public boolean communicatingAgent = false;

    AwareAgent(MazePosition position, MazeKnowledge knowledge) {
        setAID(new AID("AwareAgent", true));
        this.position = position;
        this.knowledge = knowledge;
    }

    @Override
    protected void setup() {
        super.setup();
        // init message exchange behaviour, as defined by the CFP classes
        if (communicatingAgent) {
            addBehaviour(messageBehaviour = new MessageBehaviour(this));
        }
    }

    public MazeKnowledge getKnowledge() {
        return knowledge;
    }

    /**
     * Generate own call for proposals
     *
     * @return the proposal message
     */
    public ACLMessage createCFP() {
        return null;
    }

    /**
     * Handle receiving calls for proposals
     *
     * @param msg the msg that contains the CFP
     * @return the response to the CFP
     */
    public ACLMessage handleCFP(ACLMessage msg) {
        return null;
    }

    /**
     * Handle a proposal and return either rejection or acceptance
     *
     * @param msg the incoming proposal
     * @return ACLMessage.ACCEPT_PROPOSAL or ACLMessage.REJECT_PROPOSAL
     */
    public ACLMessage handleProposal(ACLMessage msg) {
        return null;
    }

    /**
     * Handle accepted calls for proposals, return the response
     *
     * @param msg the msg that contains the CFP
     * @return an {@link ACLMessage} with my data
     */
    public ACLMessage acceptedProposal(ACLMessage msg) {
        return null;
    }


    /**
     * Handle receiving a rejection on proposal calls for proposals
     *
     * @param msg the msg that contains the CFP
     */
    public void rejectedProposal(ACLMessage msg) { }

    /**
     * Calculate the result of moving from current pos in dir direction
     *
     * @param dir the direction of the move
     * @return the new pos
     */
    Vector2D getPosAfterMove(Directions dir) {
        return position.getPosition().translate(dir.direction);
    }

    /**
     * Helper function to create an ACL message
     *
     * @param perf        the performative type of the message
     * @param receiverAID the id of the destination agent
     * @param content     the content to include
     * @return ACLMessage with the content, if possible
     */
    ACLMessage createACLMessage(int perf, AID receiverAID, Serializable content) {
        ACLMessage msg = new ACLMessage(perf);
        msg.setSender(getAID());
        msg.addReceiver(receiverAID);
        try {
            msg.setContentObject(content);
        } catch (IOException e) {
            print("failed to set content of message");
            e.printStackTrace();
        }
        return msg;
    }


    /**
     * Mandatory method to update agents
     */
    public void tick() {
        if (position.atExit()) {
            takeDown();
            doDelete();
            return;
        }
        if (isNegotiating()) return;
        knowledge.update(position.getPosition().x, position.getPosition().y, CellState.PATH);
        handleTick();
    }

    public abstract void handleTick();

    /**
     * Sets the timestamp on a message and either send to predifined destinations
     * or to all both-ways visible neighbours
     *
     * @param msg             the message
     * @param sendToNeigbours if true ignore current receivers and send to neighbours
     * @return the timestamp of the message
     */
    public long sendTimestamp(ACLMessage msg, boolean sendToNeigbours) {
        msg.setPostTimeStamp((System.currentTimeMillis() / 1000L));
        if (sendToNeigbours) {
            msg.clearAllReceiver();
            for (String name : LabyrinthModel.getNeighbours(this)) {
                print("will send to: " + name);
                msg.addReceiver(new AID(name, true));
            }
        }
        if (msg.getAllReceiver().hasNext()) {
            send(msg);
            try {
                print("sent message: " + msg.getContentObject().toString() + " at " + msg.getPostTimeStamp());
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        }
        return msg.getPostTimeStamp();
    }

    private boolean isNegotiating() {
        return messageBehaviour != null && messageBehaviour.negotiatingWith != null;
    }

    public void print(String message) {
        System.out.println("[" + getAID().getName() + "] - " + message);
    }

}
