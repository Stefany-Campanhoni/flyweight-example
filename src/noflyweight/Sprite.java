package noflyweight;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Sprite {
    private final Image image;
    private final String name;
    private int spriteCount = 0;

    public Sprite(String path, String name) {
        this.name = name;
        Image tempImage = null;
        try {
            tempImage = ImageIO.read(new File("src/" + path));
        } catch (IOException e) {
            System.out.println("Failed to load sprite: " + path);
            e.printStackTrace();
        }
        this.image = tempImage;
    }

    public Sprite(Image image, String name) {
        this.name = name;
        this.image = image;
    }

    public Sprite getSprite(String path, String name) {
        Sprite sprite = createSprite(path, name);
        System.out.println("Created new sprite: " + name);
        this.spriteCount++;
        return sprite;
    }

    public static Sprite createSprite(String path, String name) {
        try {
            return new Sprite(path, name);
        } catch (Exception e) {
            System.out.println("Failed to load sprite: " + path + " - Creating placeholder");
            return createPlaceholderSprite(name);
        }
    }

    private static Sprite createPlaceholderSprite(String name) {
        int hash = name.hashCode();
        Color color = new Color(Math.abs(hash % 255),
                                Math.abs((hash / 255) % 255),
                                Math.abs((hash / 65025) % 255));

        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        g.setColor(color);
        g.fillRect(0, 0, 16, 16);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, 15, 15);
        g.dispose();

        return new Sprite(img, name);
    }

    public Image getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
