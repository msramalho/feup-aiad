package labyrinth.agents.behaviours;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import labyrinth.agents.AwareAgent;
import sajas.core.behaviours.CyclicBehaviour;


public class MessageBehaviour extends CyclicBehaviour {
    private static final long serialVersionUID = 1L;


    private AwareAgent myAgent;
    public boolean negotiating = false;

    public MessageBehaviour(AwareAgent myAgent) {
        super(myAgent);
        this.myAgent = myAgent;
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();

        if (msg != null) {
            try {
                myAgent.print(msg.getContentObject().toString() + " from " + msg.getSender().getName() + " at " + msg.getPostTimeStamp());
            } catch (UnreadableException e) {
                e.printStackTrace();
            }

            switch (msg.getPerformative()) {
                case ACLMessage.CFP:
                    myAgent.sendTimestamp(myAgent.handleCFP(msg), false);
                    break;
                case ACLMessage.PROPOSE:
                    negotiating = true;
                    myAgent.sendTimestamp(myAgent.handleProposal(msg), false);
                    break;
                case ACLMessage.ACCEPT_PROPOSAL:
                    myAgent.sendTimestamp(myAgent.acceptedProposal(msg), false);
                    break;
                case ACLMessage.REJECT_PROPOSAL:
                    myAgent.rejectedProposal(msg);
                    break;
                case ACLMessage.AGREE: // does not break on purpose
                    myAgent.print("I received what he promised me!");
                case ACLMessage.CANCEL:
                    negotiating = false;
                    break;
            }
        } else {
            myAgent.sendTimestamp(myAgent.createCFP(), true);
        }

    }

}
