package com.flimpure.laser.level.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Tile {

    public static final Tile[] tiles = new Tile[256];
    public static final Tile floorTile = new FloorTile(6);
    public static final Tile wallTile = new WallTile(0);
    public static final Tile holeTile = new HoleTile(9);

    public Tile(int i) {
        tiles[i] = this;
    }

    public abstract void render(float delta, SpriteBatch batch, float x, float y);

    public abstract boolean isCollidable();

}
