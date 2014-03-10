package com.flimpure.laser.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.flimpure.laser.assets.Assets;

public class Player extends Entity {

	public Vector2 laserTarget;

    public Player(float x, float y, float width, float height) {
        super(x, y, width, height);
        laserTarget = new Vector2(0.0f, 0.0f);
    }

    @Override
    public void render(float delta, SpriteBatch batch) {
    	float x = x() - (width * 0.5f);
    	float y = y() - (width * 0.5f);
        batch.draw(Assets.getFullGameObject("lense_shadow"), x + 0.1f, y - 0.5f, 0.8f, 0.3f);
        batch.draw(Assets.getFullGameObject("lense"), x, y, width, height);
        batch.draw(Assets.getFullGameObject("lense_hue"), x - 1f, y - 1f, 3f, 3f);
    }

    public float x() {
    	return x;
    }

    public float y() {
    	return y;
    }
}
