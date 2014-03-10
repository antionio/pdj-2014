package com.flimpure.laser.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.flimpure.laser.assets.Assets;

public class Player extends Entity {

    public Player(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public void render(float delta, SpriteBatch batch) {
        batch.draw(Assets.getFullGameObject("lense_shadow"), x + 0.1f, y - 0.5f, 0.8f, 0.3f);
        batch.draw(Assets.getFullGameObject("lense"), x, y, width, height);
        batch.draw(Assets.getFullGameObject("lense_hue"), x - 1f, y - 1f, 3f, 3f);
    }
}
