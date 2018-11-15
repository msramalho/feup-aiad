package labyrinth;

import labyrinth.agents.AwareAgent;
import labyrinth.cli.ConfigurationFactory;
import jade.core.Profile;
import jade.core.ProfileImpl;
import labyrinth.utils.Serialization;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.ContainerController;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.gui.DisplaySurface;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LabyrinthModel extends Repast3Launcher {
    private static Map<String, AwareAgent> agents;
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
            return new ConfigurationFactory();
        }

        if (args.length > 2) {
            throw new IllegalArgumentException("Invalid usage, write a json or yaml config filename path");
        }

        String configPath = args[0];

        ConfigurationFactory config = Serialization.deserializeYamlOrJsonObject(configPath, ConfigurationFactory.class);


        String path = "data.csv";
        List<ConfigurationFactory> list = Arrays.asList(config);
        Serialization.serialize(path, list, ConfigurationFactory.class);

        return config;
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
    public static List<String> getNeighbours(AwareAgent agent) {
        return agents.entrySet().stream()
                .filter(entry -> 0 < entry.getKey().compareTo(agent.getAID().getName()))
                .filter(entry -> {
                    int dist = entry.getValue()
                            .position.getPosition()
                            .manhattanDistance(agent.position.getPosition());
                    return dist <= agent.visibility && dist <= entry.getValue().visibility;
                })
                .map(HashMap.Entry::getKey).collect(Collectors.toList());
    }
}
