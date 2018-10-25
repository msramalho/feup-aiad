package labyrinth.agents.behaviours;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import labyrinth.agents.AwareAgent;
import sajas.core.AID;
import sajas.core.behaviours.Behaviour;

import java.io.IOException;

public class MessageBehaviour extends Behaviour {
    private static final long serialVersionUID = 1L;


    private int step = 0;
    private AID neighborhoodAID;
    private ACLMessage mACLMessage;
    private AwareAgent myAgent;

    public MessageBehaviour(AwareAgent myAgent, AID neighborhoodAID, ACLMessage mACLMessage) {
        super(myAgent);
        this.myAgent = myAgent;
        this.neighborhoodAID = neighborhoodAID;
        this.mACLMessage = mACLMessage;
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if (msg == null && step == 0) {
            try {
                mACLMessage.setContentObject("OLÁ, AGENTE!");
                if (neighborhoodAID != null) mACLMessage.addReceiver(neighborhoodAID);
                else mACLMessage.addReceiver(myAgent.getAID());
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
        switch (step) {
            case 0:
                myAgent.print("step 0 in message behaviour");
                if (msg.getPerformative() == ACLMessage.CFP) {
                    try {
                        String helloString = (String) msg.getContentObject();
                        myAgent.print(helloString);
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
                } else myAgent.print("RESET MACHINE 1!!");

                break;
            case 1:
                // myAgent.print("step 1 in message behaviour");
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
                    // myAgent.print("RESET MACHINE 2!!");
                    return;
                }

                step++;
                break;
            case 2:
                myAgent.print("step 3 in message behaviour");
                if (msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                    myAgent.print("Accepted!");
                } else if (msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
                    myAgent.print("Rejected!");
                } else {
                    myAgent.print("RESET MACHINE 3!!");
                    return;
                }

                step++;
                break;
        }
    }

    @Override
    public boolean done() {
        return step == 3;
    }
}
