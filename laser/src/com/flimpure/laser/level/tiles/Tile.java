package com.flimpure.laser.level.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Tile {

    public static final Tile[] tiles = new Tile[256];
    public static final Tile floorTile = new FloorTile(0);
    public static final Tile wallTile = new WallTile(5);

    public Tile(int i) {
        tiles[i] = this;
    }

    public abstract void render(float delta, SpriteBatch batch, float x, float y);

}
