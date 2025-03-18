public class Enemy extends GameObject {
    private String type;
    private double angle = 0;

    public Enemy(int x, int y, String type) {
        super(x, y, SpriteFactory.getSprite("sprites/" + type + ".png", type));
        this.type = type;
    }

    public void move() {
        // Simple circular movement
        angle += 0.02;
        x += Math.cos(angle) * 1.0;
        y += Math.sin(angle) * 1.0;
    }
}
