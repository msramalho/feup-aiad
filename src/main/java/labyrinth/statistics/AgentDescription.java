package labyrinth.statistics;

import labyrinth.agents.AwareAgent;
import labyrinth.agents.IAwareAgent;
import labyrinth.agents.maze.MazePosition;
import labyrinth.statistics.AgentMetrics;

public class AgentDescription {
    private final MazePosition mazePosition;
    private final AwareAgent agent;
    private final String agentName;

    public AgentDescription(MazePosition mazePosition, AwareAgent agent, String agentName) {

        this.mazePosition = mazePosition;
        this.agent = agent;
        this.agentName = agentName;
    }

    public boolean atExit() {
        return mazePosition.atExit();
    }

    public String getAgentName() {
        return agentName;
    }

    public String getAgentType() {
        return agent.getClass().getSimpleName();
    }

    public AgentMetrics getAgentMetrics() {
        return new AgentMetrics();
    }
}
