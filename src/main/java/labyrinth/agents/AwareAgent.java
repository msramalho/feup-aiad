package labyrinth.agents;

import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.lang.acl.ACLMessage;
import labyrinth.agents.behaviours.MessageBehaviour;
import labyrinth.maze.Directions;
import labyrinth.agents.maze.MazeKnowledge;
import labyrinth.agents.maze.MazePosition;
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
    MazePosition position;
    MazeKnowledge knowledge;

    private SLCodec codec;
    private Ontology serviceOntology;
    private ACLMessage mACLMessage;
    private Schedule sch;
    private String request = "HELLO WORLD";

    AwareAgent(MazePosition position, MazeKnowledge knowledge, boolean isGUID) {
        setAID(new AID("AwareAgent", isGUID));
        this.position = position;
        this.knowledge = knowledge;
    }

    @Override
    protected void setup() {
        super.setup();

        // prepare cfp message
        mACLMessage = new ACLMessage(ACLMessage.CFP);

        addBehaviour(new MessageBehaviour(this, null, mACLMessage));
    }


    /**
     * Handle receiving calls for proposals
     * @param msg the msg that contains the CFP
     */
    protected void handleCFP(ACLMessage msg){ }

    /**
     * Calculate the result of moving from current pos in dir direction
     * @param dir the direction of the move
     * @return the new pos
     */
    protected Vector2D getPosAfterMove(Directions dir){
        return position.getPosition().translate(dir.direction);
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


    public void print(String message) {
        System.out.println("[Agent: " + getAID().getName() + "] - " + message);
    }

    public void setSchedule(Schedule sch) {
        this.sch = sch;
    }
}
