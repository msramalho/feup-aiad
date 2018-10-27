package labyrinth.utils;

import labyrinth.agents.maze.MazeKnowledge;
import labyrinth.maze.Directions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Specifies the information
 */
public class NegotiationEnvelope implements Serializable {

    String message;
    // public HashMap<Pair<Integer, Integer>, MazeKnowledge.CELL_STATE> proposal = new HashMap<>();// <x,y> -> info on cell
    // <x,y> -> directions to dead end and cost of it
    transient private MazeKnowledge knowledge;
    transient private HashMap<Pair<Integer, Integer>, Pair<ArrayList<Directions>, Integer>> hiddenProposal = new HashMap<>();
    public HashMap<Pair<Integer, Integer>, Pair<ArrayList<Directions>, Integer>> proposal = new HashMap<>();
    public int utility;

    public NegotiationEnvelope(String message, MazeKnowledge knowledge, int maxUtility) {
        this.message = message;
        this.knowledge = knowledge;
        utility = loadProposal(maxUtility);
    }

    private int loadProposal(int maxUtility) {
        int value = 0;
        for (Map.Entry<Pair<Integer, Integer>, Pair<ArrayList<Directions>, Integer>> de : knowledge.deadEnds.entrySet()) {
            if (value + de.getValue().r <= maxUtility) {
                value += de.getValue().r;
                hiddenProposal.put(de.getKey(), de.getValue());
            }
            if (value == maxUtility) break;
        }
        return value;
    }

    public void revealMystery() {
        proposal = hiddenProposal;
        // proposal.forEach((coord, cell_state) -> cell_state = knowledge.confidences[coord.l][coord.r].getValue());
    }

    // private ArrayList<Pair<Integer, Integer>> getRandomCoordinates(int lines, int columns, int shareHowMany) {
    //     ArrayList<Pair<Integer, Integer>> pairs = new ArrayList<>();
    //     //generate all possibilities
    //     for (int i = 0; i < lines; i++)
    //         for (int j = 0; j < columns; j++)
    //             if (knowledge.confidences[i][j].isUnknown())
    //                 pairs.add(new Pair<>(i, j));
    //     Collections.shuffle(pairs);
    //     return new ArrayList<>(pairs.subList(0, Math.min(shareHowMany, pairs.size())));
    // }
}
