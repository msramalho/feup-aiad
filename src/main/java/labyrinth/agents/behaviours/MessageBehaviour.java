package labyrinth.agents.behaviours;

import labyrinth.agents.AwareAgent;
import labyrinth.utils.ACLMessageC;
import sajas.core.behaviours.Behaviour;


enum STATE {WAITING_CFP, WAITING_PROPOSAL, WAITING_ANSWER, DONE}

public class MessageBehaviour extends Behaviour {
    private static final long serialVersionUID = 1L;


    private STATE step = STATE.WAITING_CFP;
    private AwareAgent myAgent;
    private long timeOfCFP;

    public MessageBehaviour(AwareAgent myAgent) {
        super(myAgent);
        this.myAgent = myAgent;
    }

    @Override
    public void action() {
        ACLMessageC msg = (ACLMessageC) myAgent.receive();
        if (msg == null && step == STATE.WAITING_CFP) {
            timeOfCFP = myAgent.sendTimestamp(myAgent.createCFP());
            return;
        } else if (msg == null) {
            block();
            return;
        }

        myAgent.print(msg.getContentObject().toString() + " from " + msg.getSender().getName());
        //TODO: isolate to another function, state machine function
        switch (step) {
            case WAITING_CFP:
                if (msg.getPerformative() == ACLMessageC.CFP) {
                    // If I sent my CFP after the other agent, it will ignore my CFP, and I will answer its
                    if (timeOfCFP > msg.getPostTimeStamp() ||
                            (timeOfCFP == msg.getPostTimeStamp() && 0 < myAgent.getName().compareTo(msg.getSender().getName()))) {
                        myAgent.print("I was not the one to send CFP first");
                        myAgent.sendTimestamp(myAgent.handleCFP(msg));
                    } else myAgent.print("I was the first to send CFP");

                    step = STATE.WAITING_PROPOSAL;
                    // try {
                    //     String helloString = (String) msg.getContentObject();
                    //     myAgent.print(helloString);
                    // } catch (UnreadableException e) {
                    //     e.printStackTrace();
                    // }
                    // ACLMessage reply = msg.createReply();
                    // reply.setPerformative(ACLMessage.PROPOSE);
                    // try {
                    //     reply.setContentObject("Tudo bem?");
                    // } catch (IOException e) {
                    //     e.printStackTrace();
                    // }
                } else myAgent.print("RESET MACHINE 1!!");

                break;
            case WAITING_PROPOSAL:
                // myAgent.print("step 1 in message behaviour");
                if (msg.getPerformative() == ACLMessageC.PROPOSE) {
                    myAgent.sendTimestamp(myAgent.handleProposal(msg));
                    // try {
                    //     String replyContent = (String) msg.getContentObject();
                    //     if (replyContent.equals("Tudo bem?")) {
                    //         ACLMessage accept = msg.createReply();
                    //         accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    //         accept.setContent("Sim, tudo!");
                    //         myAgent.sendTimestamp(accept);
                    //     } else {
                    //         ACLMessage reject = msg.createReply();
                    //         reject.setPerformative(ACLMessage.REJECT_PROPOSAL);
                    //         reject.setContent("Não!");
                    //         myAgent.sendTimestamp(reject);
                    //     }
                    // } catch (UnreadableException e) {
                    //     e.printStackTrace();
                    // }
                } else {
                    // myAgent.print("RESET MACHINE 2!!");
                    return;
                }
                step = STATE.WAITING_ANSWER;
                break;
            case WAITING_ANSWER:
                if (msg.getPerformative() == ACLMessageC.ACCEPT_PROPOSAL) {
                    myAgent.sendTimestamp(myAgent.acceptedProposal(msg));
                    myAgent.print("Accepted!");
                } else if (msg.getPerformative() == ACLMessageC.REJECT_PROPOSAL) {
                    myAgent.rejectedProposal(msg);
                    myAgent.print("Rejected!");
                } else return;

                step = STATE.DONE;
                break;
        }
        myAgent.print("In state: " + step.name());
    }

    @Override
    public boolean done() {
        return step == STATE.DONE;
    }
}
