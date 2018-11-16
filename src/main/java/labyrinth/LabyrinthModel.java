package labyrinth;

import labyrinth.agents.IAwareAgent;
import labyrinth.cli.ConfigurationFactory;
import jade.core.Profile;
import jade.core.ProfileImpl;
import labyrinth.utils.Files;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.ContainerController;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.gui.DisplaySurface;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LabyrinthModel extends Repast3Launcher {
    private static Map<String, IAwareAgent> agents;
    private final ConfigurationFactory config;

    public LabyrinthModel(ConfigurationFactory config) {
        super();
        this.config = config;
    }

    @Override
    public String getName() {
        return "AIAD - Labyrinth";
    }

    public int getMazeHeight() {
        return config.getMazeHeight();
    }

    public void setMazeHeight(int height) {
        config.setMazeHeight(height);
    }

    public int getMazeLength() {
        return config.getMazeLength();
    }

    public void setMazeLength(int length) {
        config.setMazeLength(length);
    }

    public int getSlownessRate() {
        return config.getSlownessRate();
    }

    public void setSlownessRate(int clocks) {
        config.setSlownessRate(clocks);
    }

    public long getSeed() {
        return config.getSeed();
    }

    public void setSeed(long seed) {
        config.setSeed(seed);
    }


    @Override
    public String[] getInitParam() {
        return new String[]{"MazeHeight", "MazeLength", "SlownessRate", "Seed"};
    }

    /**
     * runs after begin()
     */
    @Override
    protected void launchJADE() {
        Runtime rt = Runtime.instance();
        Profile p1 = new ProfileImpl();
        ContainerController mainContainer = rt.createMainContainer(p1);
        try {
            DisplaySurface displaySurf = new DisplaySurface(this, "Labyrinth Model");
            registerDisplaySurface("Labyrinth Model", displaySurf);

            agents = config.build(mainContainer, displaySurf, getSchedule());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static ConfigurationFactory buildConfiguration(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Running defaults");
            return new ConfigurationFactory();
        }

        if (args.length >= 2) {
            throw new IllegalArgumentException("Invalid usage. Check Readme");
        }

        String configPath = args[0];
        return Files.deserializeYamlOrJsonObject(configPath, ConfigurationFactory.class);
    }

    public static void main(String[] args) throws IOException {
        ConfigurationFactory config = buildConfiguration(args);

        SimInit init = new SimInit();
        init.setNumRuns(1);   // works only in batch mode
        LabyrinthModel model = new LabyrinthModel(config);
        init.loadModel(model, null, config.getBatchMode());
    }

    /**
     * Get the neighbours of an agent that are within its visibility range, that can also see it
     * If A and B can speak, only one of them will receive the address of the other
     *
     * @param agent the querying agent
     * @return a list of agent AID names
     */
    public static List<String> getNeighbours(IAwareAgent agent) {
        return agents.entrySet().stream()
                .filter(entry -> 0 < entry.getKey().compareTo(agent.getAID().getName()))
                .filter(entry -> {
                    int dist = entry.getValue()
                            .getMazePosition().getPosition()
                            .manhattanDistance(agent.getMazePosition().getPosition());
                    return dist <= agent.getVisibility() && dist <= entry.getValue().getVisibility();
                })
                .map(HashMap.Entry::getKey).collect(Collectors.toList());
    }
}
