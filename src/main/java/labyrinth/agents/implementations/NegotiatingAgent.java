package labyrinth.agents.implementations;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import labyrinth.agents.maze.knowledge.MazeKnowledge;
import labyrinth.agents.maze.MazePosition;
import labyrinth.utils.NegotiationEnvelope;

public class NegotiatingAgent extends BacktrackAgent {
    protected NegotiationEnvelope envelope;

    public NegotiatingAgent(MazePosition position, MazeKnowledge knowledge) {
        super(position, knowledge);
        communicatingAgent = true;
    }

    @Override
    public ACLMessage createCFP() {
        return createACLMessage(ACLMessage.CFP, null, "Hello, I want to trade!");
    }

    @Override
    public ACLMessage handleCFP(ACLMessage msg) {
        envelope = new NegotiationEnvelope("Me too", getKnowledge(), 100);
        System.out.println("I will be proposing: " + envelope.utility);
        return createACLMessage(ACLMessage.PROPOSE, msg.getSender(), envelope);
    }

    @Override
    public ACLMessage handleProposal(ACLMessage msg) {
        try {
            if (((NegotiationEnvelope) msg.getContentObject()).utility > 1) {
                envelope = new NegotiationEnvelope("Hope you like it", getKnowledge(), 100);
                envelope.revealMystery();
                return createACLMessage(ACLMessage.ACCEPT_PROPOSAL, msg.getSender(), envelope);
            }
        } catch (UnreadableException e) { e.printStackTrace(); }
        return createACLMessage(ACLMessage.REJECT_PROPOSAL, msg.getSender(), "You waste my time with things I already know");
    }

    @Override
    public ACLMessage acceptedProposal(ACLMessage msg) {
        try {
            getKnowledge().update((NegotiationEnvelope) msg.getContentObject());
            envelope.revealMystery();
            return createACLMessage(ACLMessage.AGREE, msg.getSender(), envelope);
        } catch (UnreadableException e) {e.printStackTrace();}
        return createACLMessage(ACLMessage.CANCEL, msg.getSender(), "Your envelope was corrupted");
    }
}
