package labyrinth.cli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WinChecker {
    private List<AgentDescription> agentDescriptions;
    private final boolean batchMode;
    private boolean allExited = false;
    private long tickCount;
    private List<String> messages = new ArrayList<>();
    private Map<String, List<Long>> agentExitsByType = new HashMap<>();

    public WinChecker(List<AgentDescription> agentDescriptions, boolean batchMode) {
        this.agentDescriptions = agentDescriptions;
        this.batchMode = batchMode;
        this.tickCount = 0;
    }

    public void tick() {
        if (allExited) {
            return;
        }

        this.tickCount++;

        agentDescriptions.removeIf(agentDesc -> {
            boolean mazeAtExit = agentDesc.atExit();
            if (mazeAtExit) {
                handleAgentExited(agentDesc);
            }
            return mazeAtExit;
        });

        if (agentDescriptions.size() == 0) {
            handleAllExited();

            allExited = true;
            if (batchMode) {
                System.out.println("Success. Exiting");
                System.exit(0);
            }
        }
    }

    private void handleAllExited() {
        System.out.println("================== At tick: " + this.tickCount + " All agents found the exit ==================");
        this.messages.forEach(System.out::println);

        this.printStats();
    }

    private void printStats() {
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

    private void handleAgentExited(AgentDescription agentDesc) {
        String msg = "@tick:" + this.tickCount + ", " + agentDesc.getAgentName() + " EXITED";
        messages.add(msg);
        System.out.println(msg);

        String agentType = agentDesc.getAgentType();
        List<Long> exits = agentExitsByType.containsKey(agentType) ? agentExitsByType.get(agentType) : new ArrayList<>();
        exits.add(this.tickCount);

        agentExitsByType.put(agentType, exits);
    }
}
