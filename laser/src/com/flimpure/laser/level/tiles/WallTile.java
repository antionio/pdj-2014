package com.flimpure.laser.level.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.flimpure.laser.assets.Assets;

public class WallTile extends Tile {
    public WallTile(int i) {
        super(i);
    }

    @Override
    public void render(float delta, SpriteBatch batch, float x, float y) {
        batch.draw(Assets.getFullGameObject("wall"), x, y, 1f, 1f);
    }
}
