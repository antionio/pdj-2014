package com.flimpure.laser.level.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.flimpure.laser.assets.Assets;

public class FloorTile extends Tile {
    public FloorTile(int i) {
        super(i);
    }

    @Override
    public void render(float delta, SpriteBatch batch, float x, float y) {
        batch.draw(Assets.getFullGameObject("floor"), x, y, 0.9f, 0.9f);
    }

    @Override
    public boolean isCollidable() {
        return false;
    }
}
