package labyrinth.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AgentMetrics {
    private long stepsToExit = 10;

    @JsonProperty
    public long getStepsToExit() {
        return stepsToExit;
    }
}
