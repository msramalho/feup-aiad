package labyrinth.utils;

import java.util.*;

public enum Directions {
    UP(new Vector2D(0, 1)),
    DOWN(new Vector2D(0, -1)),
    LEFT(new Vector2D(-1, 0)),
    RIGHT(new Vector2D(1, 0));

    public final Vector2D direction;

    Directions(Vector2D direction) {
        this.direction = direction;
    }

    private static final List<Directions> directions = Arrays.asList(Directions.class.getEnumConstants());
    public static final List<Directions> getRandomDirections() {
        List<Directions> directionsCopy = new ArrayList<>(directions);
        Collections.shuffle(directionsCopy);
        return directionsCopy;
    }

}
