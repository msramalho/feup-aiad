package labyrinth.utils;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.io.Serializable;

/**
 * Wrapper for {@link ACLMessage} that handles IOExceptions for cleaner code elsewhere
 * C stands for Clear, Concise, ...
 */
public class ACLMessageC extends ACLMessage {


    public ACLMessageC(int perf, AID aid, Serializable s) {
        super(perf);
        addReceiver(aid);
        setContentObject(s);
    }

    public ACLMessageC(ACLMessage o) {
        super(o.getPerformative());
        if(o==null)return;
        setPostTimeStamp(o.getPostTimeStamp());
        try {
            setContentObject(o.getContentObject());
            setContent(o.getContent());
            setSender(o.getSender());
            setOntology(o.getOntology());
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setContentObject(Serializable s) {
        try {
            super.setContentObject(s);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public Serializable getContentObject() {
        try {
            return super.getContentObject();
        } catch (UnreadableException e) {
            e.printStackTrace();
            return "NO MESSAGE";
        }
    }
}
