package labyrinth.agents;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import labyrinth.LabyrinthModel;
import labyrinth.agents.behaviours.MessageBehaviour;
import labyrinth.maze.Directions;
import labyrinth.agents.maze.MazeKnowledge;
import labyrinth.agents.maze.MazePosition;
import labyrinth.utils.Vector2D;
import jade.core.AID;
import sajas.core.Agent;
import uchicago.src.sim.engine.Schedule;

import java.io.IOException;
import java.io.Serializable;


/**
 * Maze Agent with some general useful skills, such as:
 * Easy interface for storing and updating maze Knowledge confidences
 * Easily receive and use messages
 */
public abstract class AwareAgent extends Agent {
    private final boolean isGUID;
    public MazePosition position;
    private MazeKnowledge knowledge;

    // private SLCodec codec;
    // private Ontology serviceOntology;
    // private String request = "HELLO WORLD";
    private Schedule sch;
    public int visibility = 100;

    AwareAgent(MazePosition position, MazeKnowledge knowledge, boolean isGUID) {
        setAID(new AID("AwareAgent", isGUID));
        this.isGUID = isGUID;
        this.position = position;
        this.knowledge = knowledge;
    }

    @Override
    protected void setup() {
        super.setup();

        // prepare cfp message
        addBehaviour(new MessageBehaviour(this));

        //TODO: after testing above code, replace by below
        // if (createCFP()!=null) {
        //     addBehaviour(new MessageBehaviour(this, null, createCFP()));
        // }
    }

    /**
     * Generate own call for proposals
     *
     * @return the proposal message
     */
    //TODO: replace by return null, as this is abstract
    public ACLMessage createCFP() {
        return createACLMessage(ACLMessage.CFP, null, "OLA, AGENTE, tens algo para mim?!");
    }

    /**
     * Handle receiving calls for proposals
     *
     * @param msg the msg that contains the CFP
     * @return the response to the CFP
     */
    public ACLMessage handleCFP(ACLMessage msg) {
        return createACLMessage(ACLMessage.PROPOSE, msg.getSender(), "Dou-te estes, e tu?");
    }

    /**
     * Handle a proposal and return either rejection or acceptance
     *
     * @param msg the incoming proposal
     * @return ACLMessage.ACCEPT_PROPOSAL or ACLMessage.REJECT_PROPOSAL
     */
    public ACLMessage handleProposal(ACLMessage msg) {
        return createACLMessage(ACLMessage.ACCEPT_PROPOSAL, msg.getSender(), "Aceito, aqui tens. Agora manda os que prometeste.");
    }

    /**
     * Handle accepted calls for proposals, return the response
     *
     * @param msg the msg that contains the CFP
     * @return an {@link ACLMessage} with my data
     */
    public ACLMessage acceptedProposal(ACLMessage msg) {
        return createACLMessage(ACLMessage.AGREE, msg.getSender(), "Obrigado, aqui vão os meus");
    }


    /**
     * Handle receiving a rejection on proposal calls for proposals
     *
     * @param msg the msg that contains the CFP
     */
    public void rejectedProposal(ACLMessage msg) {
        print("The other agent rejected me...");
    }

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
     * Inheriting classes have a message to directly process received messages
     *
     * @param msg not null ACLMessage
     */
    protected void receiveMessage(ACLMessage msg) {}


    private ACLMessage createACLMessage(int perf, AID receiverAID, Serializable content){
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
    public abstract void tick();

    public long sendTimestamp(ACLMessage msg, boolean sendToNeigbours) {
        msg.setPostTimeStamp((System.currentTimeMillis() / 1000L));
        if (sendToNeigbours) {
            msg.clearAllReceiver();
            for (String name : LabyrinthModel.getNeigbours(this)) {
                print("will send to: " + name);
                msg.addReceiver(new AID(name, isGUID));
            }
        }
        send(msg);
        try {
            print("sent message: " + msg.getContentObject().toString() + " at " + msg.getPostTimeStamp() );
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
        return msg.getPostTimeStamp();
    }

    public void print(String message) {
        System.out.println("[Agent: " + getAID().getName() + "] - " + message);
    }

    public void setSchedule(Schedule sch) {
        this.sch = sch;
    }
}
