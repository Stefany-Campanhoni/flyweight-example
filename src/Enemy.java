public class Enemy extends GameObject {
    private final String type;
    private int moveDirection = (int)(Math.random() * 4); // 0: up, 1: right, 2: down, 3: left
    private int moveCooldown = 0;
    private static final int SPEED = 2;

    public Enemy(int x, int y, String type) {
        super(x, y, SpriteFactory.getSprite("sprites/" + type + ".png", type));
        this.type = type;
    }

    public void move(GameWorld world) {
        // Decrease cooldown
        if (moveCooldown > 0) {
            moveCooldown--;
            return;
        }

        // Try to move in current direction
        int newX = x;
        int newY = y;
        
        switch (moveDirection) {
            case 0: newY -= SPEED; break; // Up
            case 1: newX += SPEED; break; // Right
            case 2: newY += SPEED; break; // Down
            case 3: newX -= SPEED; break; // Left
        }

        // Check if new position is valid (checking all corners of the sprite)
        if (world.isWalkable(newX, newY) && 
            world.isWalkable(newX + 15, newY) && 
            world.isWalkable(newX, newY + 15) && 
            world.isWalkable(newX + 15, newY + 15)) {
            // Move to new position
            x = newX;
            y = newY;
        } else {
            // Change direction randomly if hit obstacle
            moveDirection = (int)(Math.random() * 4);
            moveCooldown = 10; // Wait a bit before moving in new direction
        }

        // Randomly change direction occasionally
        if (Math.random() < 0.01) {
            moveDirection = (int)(Math.random() * 4);
            moveCooldown = 20;
        }
    }
}
