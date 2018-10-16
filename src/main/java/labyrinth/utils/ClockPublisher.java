package labyrinth.utils;

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

    public ClockPublisher subscribe(Runnable runner) {
        runners.add(runner);
        return this;
    }

    public ClockPublisher subscribe(List<Runnable> runners) {
        this.runners.addAll(runners);
        return this;
    }
}
