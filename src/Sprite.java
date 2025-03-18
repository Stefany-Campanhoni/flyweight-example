import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Sprite {
    private final Image image;
    private final String name;

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

    // Add this constructor to the Sprite class
    public Sprite(Image image, String name) {
        this.name = name;
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
