package labyrinth.agents.behaviours;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import sajas.core.AID;
import sajas.core.Agent;
import sajas.core.behaviours.CyclicBehaviour;

import java.io.IOException;

//TODO: not sure if this is worth isolating in another file (possibly not, as there may be more behaviours)
public class MessageBehaviour extends CyclicBehaviour {
    private static final long serialVersionUID = 1L;


    int step = 0;
    private AID neighborhoodAID;
    private ACLMessage mACLMessage;

    public MessageBehaviour(Agent myAgent, AID neighborhoodAID, ACLMessage mACLMessage) {
        super(myAgent);
        this.neighborhoodAID = neighborhoodAID;
        this.mACLMessage = mACLMessage;
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if(msg == null && step == 0){
            try {
                mACLMessage.setContentObject(null);
                if(neighborhoodAID != null)
                    mACLMessage.addReceiver(neighborhoodAID);
                else
                    mACLMessage.addReceiver(new AID("agent #2", AID.ISLOCALNAME));
            } catch (IOException e) {
                e.printStackTrace();
            }
            myAgent.send(mACLMessage);
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
                    step++;
                } else System.out.println("RESET MACHINE!!");

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
                            myAgent.send(accept);
                        } else {
                            ACLMessage reject = msg.createReply();
                            reject.setPerformative(ACLMessage.REJECT_PROPOSAL);
                            reject.setContent("Não!");
                            myAgent.send(reject);
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
                } else{
                    System.out.println("RESET MACHINE!!");
                    return;
                }

                step++;
                break;
        }
    }
}
