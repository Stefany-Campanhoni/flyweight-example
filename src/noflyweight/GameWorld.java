package noflyweight;

import java.util.ArrayList;
import java.util.List;

public class GameWorld {
    private final Tile[][] map;
    private final List<GameObject> entities;
    private Player player;
    private final int width;
    private final int height;

    public GameWorld(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new Tile[height][width];
        this.entities = new ArrayList<>();

        initializeMap();
        initializeEntities();
    }

    private void initializeMap() {
        // Create different types of tiles - each with its own sprite instance
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                String tileType;
                boolean walkable = true;

                if (x == 0 || y == 0 || x == width-1 || y == height-1) {
                    tileType = "wall";
                    walkable = false;
                }
                // Create three fixed lakes
                else if (isLake1(x, y) || isLake2(x, y) || isLake3(x, y)) {
                    tileType = "water";
                    walkable = false;
                } else if ((x + y) % 5 == 0) {
                    tileType = "grass";
                } else {
                    tileType = "dirt";
                }

                // Create tile with direct sprite instance - no factory
                map[y][x] = new Tile(x * 16, y * 16, tileType, walkable);
            }
        }
    }

    // Lake 1: Top-left area
    private boolean isLake1(int x, int y) {
        return x >= 5 && x <= 10 && y >= 5 && y <= 8;
    }

    // Lake 2: Center-right area
    private boolean isLake2(int x, int y) {
        int centerX = 25;
        int centerY = 12;
        int radiusSquared = 9; // ~3 tile radius
        return Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2) <= radiusSquared;
    }

    // Lake 3: Bottom area
    private boolean isLake3(int x, int y) {
        return x >= 8 && x <= 15 && y >= 18 && y <= 20;
    }

    public boolean isWalkable(int x, int y) {
        // Convert pixel coordinates to tile coordinates
        int tileX = x / 16;
        int tileY = y / 16;

        // Check if coordinates are within map bounds
        if (tileX < 0 || tileX >= width || tileY < 0 || tileY >= height) {
            return false;
        }

        // Return whether the tile is walkable
        return map[tileY][tileX].isWalkable();
    }

    private void initializeEntities() {
        // Create player with direct sprite
        player = new Player(width / 2 * 16, height / 2 * 16);

        // Create enemies - each with their own sprite instance
        String[] enemyTypes = {"moblin", "octorok", "darknut"};
        for (int i = 0; i < 100_000; i++) {
            int x = (int)(Math.random() * (width - 2) + 1) * 16;
            int y = (int)(Math.random() * (height - 2) + 1) * 16;
            String type = enemyTypes[i % enemyTypes.length];
            entities.add(new Enemy(x, y, type));
        }
    }

    public Tile[][] getMap() {
        return map;
    }

    public List<GameObject> getEntities() {
        return entities;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getSpriteCount() {
        return map.length * map[0].length + entities.size() + 1; // Total number of sprites
    }
}


