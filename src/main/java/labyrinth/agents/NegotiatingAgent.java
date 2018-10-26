package labyrinth.agents;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import labyrinth.agents.maze.MazeKnowledge;
import labyrinth.agents.maze.MazePosition;
import labyrinth.utils.NegotiationEnvelope;

public class NegotiatingAgent extends BacktrackAgent {
    protected NegotiationEnvelope envelope;

    public NegotiatingAgent(MazePosition position, MazeKnowledge knowledge) {
        super(position, knowledge);
    }

    @Override
    public ACLMessage createCFP() {
        return createACLMessage(ACLMessage.CFP, null, "Hello, I want to trade!");
    }

    @Override
    public ACLMessage handleCFP(ACLMessage msg) {
        envelope = new NegotiationEnvelope("Me too", knowledge, 10);
        System.out.println("I will be proposing: " + envelope.proposal.size());
        return createACLMessage(ACLMessage.PROPOSE, msg.getSender(), envelope);
    }

    @Override
    public ACLMessage handleProposal(ACLMessage msg) {
        try {
            if (knowledge.getUtility((NegotiationEnvelope) msg.getContentObject()) > 0.1) {
                envelope = new NegotiationEnvelope("Hope you like it", knowledge, 10);
                envelope.revealMystery();
                return createACLMessage(ACLMessage.ACCEPT_PROPOSAL, msg.getSender(), envelope);
            }
        } catch (UnreadableException e) { e.printStackTrace(); }
        return createACLMessage(ACLMessage.REJECT_PROPOSAL, msg.getSender(), "You waste my time with things I already know");
    }

    @Override
    public ACLMessage acceptedProposal(ACLMessage msg) {
        try {
            knowledge.update((NegotiationEnvelope) msg.getContentObject());
            envelope.revealMystery();
            return createACLMessage(ACLMessage.AGREE, msg.getSender(), envelope);
        } catch (UnreadableException e) {e.printStackTrace();}
        return createACLMessage(ACLMessage.CANCEL, msg.getSender(), "Your envelope was corrupted");
    }
}
