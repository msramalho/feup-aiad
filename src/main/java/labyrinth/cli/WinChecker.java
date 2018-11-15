package labyrinth.cli;

import labyrinth.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Follows a publisher-subscriber pattern
 */
public class WinChecker {
    private List<AgentDescription> agentDescriptions;
    private final boolean batchMode;
    private boolean allExited = false;
    private long tickCount;

    private List<Consumer<Pair<AgentDescription, Long>>> agentExitedHandler = new ArrayList<>();
    private List<Consumer<Long>> allAgentsExitedHandlers = new ArrayList<>();

    public WinChecker(List<AgentDescription> agentDescriptions, boolean batchMode) {
        this.agentDescriptions = agentDescriptions;
        this.batchMode = batchMode;
        this.tickCount = 0;
    }

    public WinChecker addAgentExitedHandler(Consumer<Pair<AgentDescription, Long>> consumer) {
        agentExitedHandler.add(consumer);
        return this;
    }

    public WinChecker addAllAgentsExitedHandler(Consumer<Long> consumer) {
        allAgentsExitedHandlers.add(consumer);
        return this;
    }

    public void tick() {
        if (allExited) {
            return;
        }

        this.tickCount++;

        agentDescriptions.removeIf(agentDesc -> {
            boolean mazeAtExit = agentDesc.atExit();
            if (mazeAtExit) {
                agentExitedHandler.forEach(c -> c.accept(new Pair<>(agentDesc, this.tickCount)));
            }
            return mazeAtExit;
        });

        if (agentDescriptions.size() == 0) {
            allAgentsExitedHandlers.forEach(c -> c.accept(this.tickCount));

            allExited = true;
            if (batchMode) {
                System.exit(0);
            }
        }
    }
}
