import java.util.HashMap;
import java.util.Map;

public class SpriteFactory {
    private static final Map<String, Sprite> sprites = new HashMap<>();

    public static Sprite getSprite(String path, String name) {
        if (!sprites.containsKey(name)) {
            sprites.put(name, createSprite(path, name));
            System.out.println("Created new sprite: " + name);
        } else {
            System.out.println("Reusing sprite: " + name);
        }
        return sprites.get(name);
    }

    private static Sprite createSprite(String path, String name) {
        try {
            return new Sprite(path, name);
        } catch (Exception e) {
            System.out.println("Failed to load sprite: " + path);
            throw e;
        }
    }

    public static int getSpriteCount() {
        return sprites.size();
    }
}