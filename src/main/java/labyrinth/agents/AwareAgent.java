package labyrinth.agents;

import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import labyrinth.maze.Directions;
import labyrinth.agents.maze.MazeKnowledge;
import labyrinth.agents.maze.MazePosition;
import labyrinth.utils.Vector2D;
import sajas.core.AID;
import sajas.core.Agent;
import sajas.core.behaviours.*;
import serviceConsumerProviderVis.onto.ServiceOntology;
import sun.nio.cs.ext.MS874;
import uchicago.src.sim.engine.Schedule;

import java.io.IOException;


/**
 * Maze Agent with some general useful skills, such as:
 * Easy interface for storing and updating maze Knowledge confidences
 * Easily receive and use messages
 */
public abstract class AwareAgent extends Agent {
    MazePosition mazePosition;
    MazeKnowledge knowledge;

    private SLCodec codec;
    private Ontology serviceOntology;
    private ACLMessage mACLMessage;
    private Schedule sch;
    private String request = "HELLO WORLD";

    AwareAgent(MazePosition mazePosition, MazeKnowledge knowledge) {
        this.mazePosition = mazePosition;
        this.knowledge = knowledge;
    }

    @Override
    protected void setup() {
        super.setup();

        // register language and ontology
        codec = new SLCodec();
        serviceOntology = ServiceOntology.getInstance();
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(serviceOntology);

        // prepare cfp message
        mACLMessage = new ACLMessage(ACLMessage.CFP);
        mACLMessage.setLanguage(codec.getName());
        mACLMessage.setOntology(serviceOntology.getName());
        mACLMessage.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);

        addBehaviour(new MessageBehaviour(this, null));
    }


    //TODO: not sure if this is worth isolating in another file (possibly not, as there may be more behaviours)
    class MessageBehaviour extends Behaviour {
        private static final long serialVersionUID = 1L;

        int step = 0;
        private AID neighborhoodAID;

        MessageBehaviour(Agent myAgent, AID neighborhoodAID) {
            super(myAgent);
            this.neighborhoodAID = neighborhoodAID;
        }

        @Override
        public void action() {
            ACLMessage msg = myAgent.receive();
            if(msg == null && step == 0){
                try {
                    mACLMessage.setContentObject(((AwareAgent) myAgent).request);
                    if(neighborhoodAID != null)
                        mACLMessage.addReceiver(neighborhoodAID);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                send(mACLMessage);
                return;
            } else if (msg == null) {
                block();
                return;
            }

            //TODO: isolate to another function, state machine function
            switch(step) {
                case 0:
                    System.out.println("0");
                    if (msg.getPerformative() == ACLMessage.CFP) {
                        try {
                            String helloString = (String) msg.getContentObject();
                            System.out.println(helloString);
                        } catch (UnreadableException e) {
                            e.printStackTrace();
                        }
                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.PROPOSE);
                        try {
                            reply.setContentObject("Tudo bem?");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        myAgent.send(reply);
                    } else System.out.println("RESET MACHINE!!");

                    step++;
                    break;
                case 1:
                    System.out.println("1");
                    if (msg.getPerformative() == ACLMessage.PROPOSE) {
                        try {
                            String replyContent = (String) msg.getContentObject();
                            if (replyContent.equals("Tudo bem?")) {
                                ACLMessage accept = msg.createReply();
                                accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                                accept.setContent("Sim, tudo!");
                                send(accept);
                            } else {
                                ACLMessage reject = msg.createReply();
                                reject.setPerformative(ACLMessage.REJECT_PROPOSAL);
                                reject.setContent("Não!");
                                send(reject);
                            }
                        } catch (UnreadableException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("RESET MACHINE!!");
                        return;
                    }

                    step++;
                    break;
                case 2:
                    System.out.println("2");
                    if (msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                        System.out.println("Accepted!");
                    } else if(msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
                        System.out.println("Rejected!");
                    } else System.out.println("RESET MACHINE!!");

                    step++;
                    break;
            }
        }

        @Override
        public boolean done() {
            return step == 3;
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


    public void setSchedule(Schedule sch) {
        this.sch = sch;
    }
}
