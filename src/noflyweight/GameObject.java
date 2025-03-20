package noflyweight;

import java.awt.*;

public abstract class GameObject {
    protected int x, y;
    protected Sprite sprite;

    public GameObject(int x, int y, Sprite sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    public void draw(Graphics g) {
        g.drawImage(sprite.getImage(), x, y, null);
    }
}
