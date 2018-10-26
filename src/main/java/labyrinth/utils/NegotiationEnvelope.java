package labyrinth.utils;

import labyrinth.agents.maze.MazeKnowledge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Specifies the information
 */
public class NegotiationEnvelope implements Serializable {

    String message;
    public HashMap<Pair<Integer, Integer>, MazeKnowledge.CELL_STATE> proposal = new HashMap<>();// <x,y> -> info on cell
    transient private MazeKnowledge knowledge;

    public NegotiationEnvelope(String message, MazeKnowledge knowledge, int shareHowMany) {
        this.message = message;
        this.knowledge = knowledge;
        ArrayList<Pair<Integer, Integer>> coordinates = getRandomCoordinates(knowledge.lines, knowledge.columns, shareHowMany);

        coordinates.forEach(coord -> {
            proposal.put(coord, MazeKnowledge.CELL_STATE.MISTERY);
        });
    }

    public void revealMystery() {
        proposal.forEach((coord, cell_state) -> cell_state = knowledge.confidences[coord.l][coord.r].getValue());
    }

    private ArrayList<Pair<Integer, Integer>> getRandomCoordinates(int lines, int columns, int shareHowMany) {
        ArrayList<Pair<Integer, Integer>> pairs = new ArrayList<>();
        //generate all possibilities
        for (int i = 0; i < lines; i++)
            for (int j = 0; j < columns; j++)
                if (knowledge.confidences[i][j].isUnknown())
                    pairs.add(new Pair<>(i, j));
        Collections.shuffle(pairs);
        return new ArrayList<>(pairs.subList(0, Math.min(shareHowMany, pairs.size())));
    }
}
