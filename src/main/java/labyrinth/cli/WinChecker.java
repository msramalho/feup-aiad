package labyrinth.cli;

import labyrinth.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Follows a publisher-subscriber pattern
 */
public class WinChecker {
    private List<AgentDescription> agentDescriptions;
    private List<AgentDescription> totalAgents;
    private boolean batchMode;
    private boolean allExited = false;
    private long tickCount;

    private List<BiConsumer<AgentDescription, Long>> agentExitedHandler = new ArrayList<>();
    private List<BiConsumer<List<AgentDescription>, Long>> allAgentsExitedHandlers = new ArrayList<>();

    public WinChecker(List<AgentDescription> agentDescriptions, boolean batchMode) {
        this.agentDescriptions = agentDescriptions;
        this.totalAgents = new ArrayList<>(agentDescriptions);
        this.batchMode = batchMode;
        this.tickCount = 0;
    }

    public WinChecker addAgentExitedHandler(BiConsumer<AgentDescription, Long> consumer) {
        agentExitedHandler.add(consumer);
        return this;
    }

    public WinChecker addAllAgentsExitedHandler(BiConsumer<List<AgentDescription>, Long> consumer) {
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
                agentExitedHandler.forEach(c -> c.accept(agentDesc, this.tickCount));
            }
            return mazeAtExit;
        });

        if (agentDescriptions.size() == 0) {
            allAgentsExitedHandlers.forEach(c -> c.accept(totalAgents, this.tickCount));

            allExited = true;
            if (batchMode) {
                System.exit(0);
            }
        }
    }
}
