package labyrinth.agents.behaviours;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import labyrinth.agents.AwareAgent;
import sajas.core.behaviours.Behaviour;


enum STATE {WAITING_CFP, WAITING_PROPOSAL, WAITING_ANSWER, WAITING_INFO, DONE}

public class MessageBehaviour extends Behaviour {
    private static final long serialVersionUID = 1L;


    private STATE step = STATE.WAITING_CFP;
    private AwareAgent myAgent;
    private long timeOfCFP;
    boolean sentMessage = false;

    public MessageBehaviour(AwareAgent myAgent) {
        super(myAgent);
        this.myAgent = myAgent;
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        // ACLMessage msg = new ACLMessage(myAgent.receive());
        if (msg == null && step == STATE.WAITING_CFP) {
            timeOfCFP = myAgent.sendTimestamp(myAgent.createCFP(), true);
            step = STATE.WAITING_PROPOSAL;
            sentMessage = true;
            return;
        } else if (msg == null) {
            block();
            return;
        }
        if (msg.getSender().getName().equals(myAgent.getAID().getName())) {
            myAgent.print("Will ignore own message");
            return;
        }

        try {
            myAgent.print(msg.getContentObject().toString() + " from " + msg.getSender().getName() + " at " + msg.getPostTimeStamp());
        } catch (UnreadableException e) {
            e.printStackTrace();
        }




        //TODO: isolate to another function, state machine function
        switch (step) {
            case WAITING_CFP:
                if (msg.getPerformative() == ACLMessage.CFP) {
                    // If I sent my CFP after the other agent, it will ignore my CFP, and I will answer its
                    if (!sentMessage || 0 > myAgent.getAID().getName().compareTo(msg.getSender().getName())) {
                        myAgent.print("I was not the one to send CFP first");
                        myAgent.sendTimestamp(myAgent.handleCFP(msg), false);
                    } else myAgent.print("I was the first to send CFP");

                    step = STATE.WAITING_PROPOSAL;
                } else myAgent.print("RESET MACHINE 1!!");
                //no break on purpose
            case WAITING_PROPOSAL:
                if (msg.getPerformative() == ACLMessage.PROPOSE) {
                    myAgent.sendTimestamp(myAgent.handleProposal(msg), false);
                    step = STATE.WAITING_INFO;
                } else {
                    step = STATE.WAITING_ANSWER;
                    return;
                }
                break;
            case WAITING_ANSWER:
                if (msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                    myAgent.sendTimestamp(myAgent.acceptedProposal(msg), false);
                    myAgent.print("Accepted!");
                } else if (msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
                    myAgent.rejectedProposal(msg);
                    myAgent.print("Rejected!");
                } else return;

                step = STATE.DONE;
                break;
            case WAITING_INFO:
                if (msg.getPerformative() == ACLMessage.AGREE) {
                    myAgent.print("I received what he promised me!");
                    step = STATE.DONE;
                }
                break;

        }
        myAgent.print("In state: " + step.name());
    }

    @Override
    public boolean done() {
        return step == STATE.DONE;
    }
}
