package noflyweight;

public class Player extends GameObject {
    public Player(int x, int y) {
        super(x, y, Sprite.createSprite("sprites/link.png", "player"));
    }

    public void move(int dx, int dy, GameWorld world) {
        int newX = x + dx;
        int newY = y + dy;

        // Check if the new position is walkable (checking all corners of 16x16 sprite)
        if (world.isWalkable(newX, newY) &&
                world.isWalkable(newX + 15, newY) &&
                world.isWalkable(newX, newY + 15) &&
                world.isWalkable(newX + 15, newY + 15)) {
            this.x = newX;
            this.y = newY;
        }
    }
}
