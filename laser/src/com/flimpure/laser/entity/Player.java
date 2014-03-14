package com.flimpure.laser.entity;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Cubic;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.flimpure.laser.assets.Assets;
import com.flimpure.laser.screen.GameScreen;
import com.flimpure.laser.tween.Vector2Accessor;

public class Player extends Entity {

	/** The point where the player's laser currently fires at. */
	public Vector2 laserTarget;
	
	/** Whether player is currently hitting an enemy. */
	public boolean hitsEnemy;

    private final TweenManager tweenManager = new TweenManager();
    private final Vector2 floatOffset = new Vector2();

    public Player(float x, float y, float width, float height, GameScreen gameScreen) {
        super(x, y, width, height, gameScreen);
        laserTarget = new Vector2(0.0f, 0.0f);
        hitsEnemy = false;

        Tween.to(floatOffset, Vector2Accessor.POSITION_Y, 1f).target(0.05f)
                .ease(Cubic.INOUT).repeatYoyo(Tween.INFINITY, 0f).start(tweenManager);
    }

    @Override
    public void update(float fixedStep) {
        tweenManager.update(fixedStep);
        super.update(fixedStep);
    }

    @Override
    public void render(float delta, SpriteBatch batch) {
    	float x = x() - (width * 0.5f);
    	float y = y() - (width * 0.5f);
        batch.draw(Assets.getFullGameObject("lense_shadow"), x + 0.15f, y - 0.5f, 0.7f, 0.35f);
        batch.draw(Assets.getFullGameObject("lense"), x, y + floatOffset.y, width, height);
        batch.draw(Assets.getFullGameObject("lense_hue"), x - 1f, y - 1f, 3f, 3f);
    }

    public float x() {
    	return x;
    }

    public float y() {
    	return y;
    }
}
