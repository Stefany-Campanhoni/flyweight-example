package noflyweight;

public class Tile extends GameObject {
    private boolean walkable;

    public Tile(int x, int y, String type, boolean walkable) {
        super(x, y, Sprite.createSprite("sprites/tiles/" + type + ".png", "tile_" + type));
        this.walkable = walkable;
    }

    public boolean isWalkable() {
        return walkable;
    }
}
