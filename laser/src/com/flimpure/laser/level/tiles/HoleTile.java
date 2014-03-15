package com.flimpure.laser.level.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HoleTile extends Tile {
    public HoleTile(int i) {
        super(i);
    }

    @Override
    public void render(float delta, SpriteBatch batch, float x, float y) {
        // render nothing
    }

    @Override
    public boolean isCollidable() {
        return false;
    }
}
