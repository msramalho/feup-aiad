package labyrinth.maze;

import labyrinth.utils.Vector2D;

import java.util.*;

public enum Directions {
    UP(new Vector2D(0, -1)),
    DOWN(new Vector2D(0, 1)),
    LEFT(new Vector2D(-1, 0)),
    RIGHT(new Vector2D(1, 0));

    public final Vector2D direction;

    Directions(Vector2D direction) {
        this.direction = direction;
    }

    public static final List<Directions> directions = Arrays.asList(Directions.values());

    public static List<Directions> getRandomDirections() {
        List<Directions> directionsCopy = new ArrayList<>(directions);
        Collections.shuffle(directionsCopy);
        return directionsCopy;
    }

    public static Directions getOpposite(Directions d1) {
        if (d1 == UP) return DOWN;
        if (d1 == DOWN) return UP;
        if (d1 == LEFT) return RIGHT;
        if (d1 == RIGHT) return LEFT;
        return null;
    }


    //TODO: maybe remove after debug is done OR use reflection to simplify
    @Override
    public String toString() {
        if (direction == UP.direction) return "UP";
        if (direction == DOWN.direction) return "DOWN";
        if (direction == RIGHT.direction) return "RIGHT";
        if (direction == LEFT.direction) return "LEFT";
        return "";
    }
}
