import java.util.ArrayList;
import java.util.List;

public class GameWorld {
    private final Tile[][] map;
    private final ArrayList<GameObject> entities;
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
        // Create different types of tiles but reuse sprites
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
        // Create player
        player = new Player(width / 2 * 16, height / 2 * 16);

        // Create only 3 enemies initially - one of each type
        String[] enemyTypes = {"moblin", "octorok", "darknut"};
        for (int i = 0; i < 3; i++) {
            int x, y;
            boolean validPosition;
            
            do {
                x = (int)(Math.random() * (width - 2) + 1) * 16;
                y = (int)(Math.random() * (height - 2) + 1) * 16;
                validPosition = isWalkable(x, y);
            } while (!validPosition);
            
            String type = enemyTypes[i % enemyTypes.length];
            entities.add(new Enemy(x, y, type));
        }
    }

    public void addEnemy(Enemy enemy) {
        entities.add(enemy);
    }

    // For spawning new enemies
    public Enemy spawnEnemy() {
        String[] enemyTypes = {"moblin", "octorok", "darknut"};
        int x, y;
        boolean validPosition;
        
        do {
            x = (int)(Math.random() * (width - 2) + 1) * 16;
            y = (int)(Math.random() * (height - 2) + 1) * 16;
            validPosition = isWalkable(x, y);
        } while (!validPosition);
        
        String type = enemyTypes[(int)(Math.random() * enemyTypes.length)];
        Enemy enemy = new Enemy(x, y, type);
        entities.add(enemy);
        return enemy;
    }

    public Tile[][] getMap() {
        return map;
    }

    public synchronized List<GameObject> getEntities() {
        return (List<GameObject>) this.entities.clone();
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
}
