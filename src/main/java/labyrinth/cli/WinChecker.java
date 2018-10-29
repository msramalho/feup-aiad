package labyrinth.cli;

import labyrinth.agents.maze.MazePosition;

import java.util.List;

public class WinChecker {
    private List<MazePosition> mazePositions;
    private final boolean batchMode;
    private boolean allExited = false;

    public WinChecker(List<MazePosition> mazePositions, boolean batchMode) {
        this.mazePositions = mazePositions;
        this.batchMode = batchMode;
    }

    public void tick() {
        if (allExited) {
            return;
        }

        mazePositions.removeIf(maze -> {
            boolean mazeAtExit = maze.atExit();
            if (mazeAtExit) {
                System.out.println("An agent found the exit");
            }

            return mazeAtExit;
        });

        if (mazePositions.size() == 0) {
            System.out.println("======= All agents found the exit ==================");
            allExited = true;
            if (batchMode) {
                System.out.println("Success. Exiting");
                System.exit(0);
            }
        }
    }
}
