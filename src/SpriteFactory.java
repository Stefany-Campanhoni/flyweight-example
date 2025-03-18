import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class SpriteFactory {
    private static final Map<String, Sprite> sprites = new HashMap<>();

    public static Sprite getSprite(String path, String name) {
        String key = name;
        if (!sprites.containsKey(key)) {
            sprites.put(key, createSprite(path, name));
            System.out.println("Created new sprite: " + name);
        } else {
            System.out.println("Reusing sprite: " + name);
        }
        return sprites.get(key);
    }

    private static Sprite createSprite(String path, String name) {
        try {
            return new Sprite(path, name);
        } catch (Exception e) {
            System.out.println("Failed to load sprite: " + path + " - Creating placeholder");
            return createPlaceholderSprite(name);
        }
    }

    private static Sprite createPlaceholderSprite(String name) {
        // Create colored square based on name hash
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

    public static int getSpriteCount() {
        return sprites.size();
    }
}