package labyrinth.agents.behaviours;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import labyrinth.agents.AwareAgent;
import sajas.core.behaviours.CyclicBehaviour;


enum STATE {WAITING_CFP, WAITING_PROPOSAL, WAITING_ANSWER, WAITING_INFO, DONE}

public class MessageBehaviour extends CyclicBehaviour {
    private static final long serialVersionUID = 1L;


    private STATE step = STATE.WAITING_CFP;
    private AwareAgent myAgent;
    boolean sentMessage = false;

    public MessageBehaviour(AwareAgent myAgent) {
        super(myAgent);
        this.myAgent = myAgent;
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        //LOL: WARNING: SAJaS does not currently support blocking approaches -- returning null

        if (msg != null) {
            try {
                myAgent.print(msg.getContentObject().toString() + " from " + msg.getSender().getName() + " at " + msg.getPostTimeStamp());
            } catch (UnreadableException e) {
                e.printStackTrace();
            }

            // OPTION 1, I am the one receiving the Call for Proposal
            if (msg.getPerformative() == ACLMessage.CFP) {
                ACLMessage response = myAgent.handleCFP(msg);
                myAgent.sendTimestamp(response, false);
                if (response.getPerformative() != ACLMessage.PROPOSE) return; //If I did not propose anything

                //if he accepts, then I handle it
                myAgent.print("sent Proposal, waiting for next message");
                msg = myAgent.receive();
                myAgent.print("Got next message: ");
                try {
                    myAgent.print("it is " + msg==null?"null": msg.getContentObject().toString());
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
                if (msg == null || msg.getPerformative() != ACLMessage.ACCEPT_PROPOSAL) return;

                //if he accepts, then I handle it
                myAgent.sendTimestamp(myAgent.acceptedProposal(msg), false);
            }

            // switch (msg.getPerformative()) {
            //     case ACLMessage.CFP:
            //         myAgent.sendTimestamp(myAgent.handleCFP(msg), false);
            //         break;
            //     case ACLMessage.PROPOSE:
            //         myAgent.sendTimestamp(myAgent.handleProposal(msg), false);
            //         break;
            //     case ACLMessage.ACCEPT_PROPOSAL:
            //         myAgent.sendTimestamp(myAgent.acceptedProposal(msg), false);
            //         break;
            //     case ACLMessage.REJECT_PROPOSAL:
            //         myAgent.rejectedProposal(msg);
            //         break;
            //     case ACLMessage.AGREE:
            //         myAgent.print("I received what he promised me!");
            //         break;
            // }
        } else {
            // OPTION 2, I am the one proposing
            myAgent.sendTimestamp(myAgent.createCFP(), true);
            myAgent.print("sent CFP, waiting for next message");
            msg = myAgent.receive();
            myAgent.print("Got next message: ");
            myAgent.print("it is " + msg==null?"null":" not null");
            if (msg == null || msg.getPerformative() != ACLMessage.PROPOSE) return;

            ACLMessage response = myAgent.handleProposal(msg);
            myAgent.sendTimestamp(response, false);
            if (response.getPerformative() != ACLMessage.ACCEPT_PROPOSAL) return; //If I did not accept

            //if I accepted, then he should send his info
            msg = myAgent.receive();
            if (msg != null && msg.getPerformative() == ACLMessage.AGREE) {
                myAgent.print("I received what he promised me!");
            }
        }

        // if (msg == null && step == STATE.WAITING_CFP) {
        //     myAgent.sendTimestamp(myAgent.createCFP(), true);
        //     step = STATE.WAITING_PROPOSAL;
        //     sentMessage = true;
        //     return;
        // } else if (msg == null) {
        //     block();
        //     return;
        // }
        // if (msg.getSender().getName().equals(myAgent.getAID().getName())) {
        //     myAgent.print("Will ignore own message");
        //     return;
        // }


        // //TODO: isolate to another function, state machine function
        // switch (step) {
        //     case WAITING_CFP:
        //         if (msg.getPerformative() == ACLMessage.CFP) {
        //             // If I sent my CFP after the other agent, it will ignore my CFP, and I will answer its
        //             if (!sentMessage || 0 > myAgent.getAID().getName().compareTo(msg.getSender().getName())) {
        //                 myAgent.print("I was not the one to send CFP first");
        //                 myAgent.sendTimestamp(myAgent.handleCFP(msg), false);
        //             } else myAgent.print("I was the first to send CFP");
        //
        //             step = STATE.WAITING_PROPOSAL;
        //         } else myAgent.print("RESET MACHINE 1!!");
        //         //no break on purpose
        //     case WAITING_PROPOSAL:
        //         if (msg.getPerformative() == ACLMessage.PROPOSE) {
        //             myAgent.sendTimestamp(myAgent.handleProposal(msg), false);
        //             step = STATE.WAITING_INFO;
        //         } else {
        //             step = STATE.WAITING_ANSWER;
        //             return;
        //         }
        //         break;
        //     case WAITING_ANSWER:
        //         if (msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
        //             myAgent.sendTimestamp(myAgent.acceptedProposal(msg), false);
        //             myAgent.print("Accepted!");
        //         } else if (msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
        //             myAgent.rejectedProposal(msg);
        //             myAgent.print("Rejected!");
        //         } else return;
        //
        //         step = STATE.WAITING_CFP;
        //         break;
        //     case WAITING_INFO:
        //         if (msg.getPerformative() == ACLMessage.AGREE) {
        //             myAgent.print("I received what he promised me!");
        //             step = STATE.WAITING_CFP;
        //         }
        //         break;
        //
        // }
        // myAgent.print("In state: " + step.name());
    }

}
