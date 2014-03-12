package com.flimpure.laser.level.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.flimpure.laser.assets.Assets;

public class FloorTile extends Tile {
    public FloorTile(int i) {
        super(i);
    }

    @Override
    public void render(float delta, SpriteBatch batch, float x, float y) {
        batch.draw(Assets.getGameObject("floor"), x, y, 1f, 1f);
    }

    @Override
    public boolean isCollidable() {
        return false;
    }
}
