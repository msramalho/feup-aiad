package labyrinth.agents;

import uchicago.src.sim.engine.BasicAction;

import java.util.ArrayList;
import java.util.List;

public class ClockPublisher extends BasicAction {

    List<Runnable> runners = new ArrayList<>();

    @Override
    public void execute() {
        for (Runnable runner : runners) {
            runner.run();
        }
    }

    public void subscribe(Runnable runner) {
        runners.add(runner);
    }
}
