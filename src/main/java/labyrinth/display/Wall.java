package labyrinth.display;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

public class Wall implements Drawable {

    private static final String IMAGE_LOCATION = "resources/wall.png";

    private int x;
    private int y;
    private BufferedImage image;

    public Wall(int x, int y) {
        this.x = x;
        this.y = y;

        try {
            this.image = ImageIO.read(new File(IMAGE_LOCATION));
        } catch (IOException e) {
            System.out.println("Couldn't load wall image");
        }
    }

    @Override
    public void draw(SimGraphics G) {
        G.drawImageToFit(image);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

}
