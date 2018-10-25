package labyrinth.agents;

import labyrinth.LabyrinthModel;
import labyrinth.agents.behaviours.MessageBehaviour;
import labyrinth.maze.Directions;
import labyrinth.agents.maze.MazeKnowledge;
import labyrinth.agents.maze.MazePosition;
import labyrinth.utils.ACLMessageC;
import labyrinth.utils.Vector2D;
import sajas.core.AID;
import sajas.core.Agent;
import uchicago.src.sim.engine.Schedule;


/**
 * Maze Agent with some general useful skills, such as:
 * Easy interface for storing and updating maze Knowledge confidences
 * Easily receive and use messages
 */
public abstract class AwareAgent extends Agent {
    public MazePosition position;
    private MazeKnowledge knowledge;

    // private SLCodec codec;
    // private Ontology serviceOntology;
    // private String request = "HELLO WORLD";
    private Schedule sch;
    public int visibility = 100;

    AwareAgent(MazePosition position, MazeKnowledge knowledge, boolean isGUID) {
        setAID(new AID("AwareAgent", isGUID));
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
    public ACLMessageC createCFP() {
        return new ACLMessageC(ACLMessageC.CFP, getAID(), "OLÁ, AGENTE, tens algo para mim?!");
    }

    /**
     * Handle receiving calls for proposals
     *
     * @param msg the msg that contains the CFP
     * @return the response to the CFP
     */
    public ACLMessageC handleCFP(ACLMessageC msg) {
        return new ACLMessageC(ACLMessageC.PROPOSE, getAID(), "Dou-te estes, e tu?");
    }

    /**
     * Handle a proposal and return either rejection or acceptance
     *
     * @param msg the incoming proposal
     * @return ACLMessageC.ACCEPT_PROPOSAL or ACLMessageC.REJECT_PROPOSAL
     */
    public ACLMessageC handleProposal(ACLMessageC msg) {
        return new ACLMessageC(ACLMessageC.ACCEPT_PROPOSAL, getAID(), "Aceito, aqui tens. Agora manda os que prometeste.");
    }

    /**
     * Handle accepted calls for proposals, return the response
     *
     * @param msg the msg that contains the CFP
     * @return an {@link ACLMessageC} with my data
     */
    public ACLMessageC acceptedProposal(ACLMessageC msg) {
        return new ACLMessageC(ACLMessageC.AGREE, getAID(), "Obrigado, aqui vão os meus");
    }


    /**
     * Handle receiving a rejection on proposal calls for proposals
     *
     * @param msg the msg that contains the CFP
     */
    public void rejectedProposal(ACLMessageC msg) {
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
     * @param msg not null ACLMessageC
     */
    protected void receiveMessage(ACLMessageC msg) {}

    /**
     * Mandatory method to update agents
     */
    public abstract void tick();

    public long sendTimestamp(ACLMessageC msg) {
        msg.setPostTimeStamp((System.currentTimeMillis() / 1000L));
        // for (Object a : LabyrinthModel.getNeigbours(this)) {
        //     msg.addReceiver((jade.core.AID) a);
        // }
        send(msg);
        return msg.getPostTimeStamp();
    }

    public void print(String message) {
        System.out.println("[Agent: " + getAID().getName() + "] - " + message);
    }

    public void setSchedule(Schedule sch) {
        this.sch = sch;
    }
}
