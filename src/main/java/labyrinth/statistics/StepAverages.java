package labyrinth.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * To be used as a subscriber to WinChecker events
 */
public class StepAverages {

    private List<String> messages = new ArrayList<>();
    private Map<String, List<Long>> agentExitsByType = new HashMap<>();
    
    public StepAverages() {
        
    }

    public void oneAgentExited(AgentDescription agentDesc, Long tickAtExit) {
        String msg = "@tick:" + tickAtExit + ", " + agentDesc.getAgentName() + " EXITED";
        messages.add(msg);
        System.out.println(msg);

        String agentType = agentDesc.getAgentType();
        List<Long> exits = agentExitsByType.containsKey(agentType) ? agentExitsByType.get(agentType) : new ArrayList<>();
        exits.add(tickAtExit);

        agentExitsByType.put(agentType, exits);
    }

    public void allAgentsExited(List<AgentDescription> unused, Long tickAtExit) {
        System.out.println("================== At tick: " + tickAtExit + " All implementations found the exit ==================");
        this.messages.forEach(System.out::println);

        Map<String, Double> averages = this.agentExitsByType.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, pair -> pair.getValue().stream()
                        .reduce((sum, x) -> sum+x)
                        .get() /
                        (double) pair.getValue()
                                .size()));

        averages.forEach((k, v) -> {
            System.out.println("Agent type: " + k + " average ticks to exit: " + v);
        });

    }
    
}
