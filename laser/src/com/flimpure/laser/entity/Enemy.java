package com.flimpure.laser.entity;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Cubic;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.flimpure.laser.assets.Assets;
import com.flimpure.laser.entity.Player.Beam;
import com.flimpure.laser.screen.GameScreen;
import com.flimpure.laser.tween.Vector2Accessor;

public class Enemy extends Entity {

	private EnemyColor color;
	private final TweenManager tweenManager = new TweenManager();
	private final Vector2 floatOffset = new Vector2();

	public Enemy(float x, float y, EnemyColor color, GameScreen gameScreen) {
		super(x, y, 1f, 1f, gameScreen);
		this.color = color;

		Tween.to(floatOffset, Vector2Accessor.POSITION_Y, 1f).target(0.05f)
				.ease(Cubic.INOUT).repeatYoyo(Tween.INFINITY, 0f)
				.start(tweenManager);
	}

	private final Vector2 playerPos = new Vector2();
	private final Vector2 enemyPos = new Vector2();

	@Override
	public float getMaxVelocity() {
		return 2f;
	}

	@Override
	public void update(float fixedStep) {
		tweenManager.update(fixedStep);
		super.update(fixedStep);

		playerPos.set(gameScreen.player.x, gameScreen.player.y);
		enemyPos.set(x, y);

		accel.set(playerPos.sub(enemyPos));
	}

	@Override
	public void onHit(Player player) {
		super.onHit(player);
		
		if (player.currentBeam.equals(Beam.BLUE) && color.equals(EnemyColor.RED)) {
			accel.scl(0.25f);
			takeDamage(1.0f);			
		}
		if (player.currentBeam.equals(Beam.BLUE) && color.equals(EnemyColor.BLUE)) {
			accel.scl(1.25f);
		}
		
		if (player.currentBeam.equals(Beam.RED) && color.equals(EnemyColor.BLUE)) {
			accel.scl(0.25f);
			takeDamage(1.0f);	
		}
		if (player.currentBeam.equals(Beam.RED) && color.equals(EnemyColor.RED)) {
			accel.scl(1.25f);		
		}
		
	}

	@Override
	public void render(float delta, SpriteBatch batch) {
		float x = this.x - width * 0.5f;
		float y = this.y - height * 0.5f;

		batch.draw(Assets.getFullGameObject("enemy_shadow"), x, y - 0.5f,
				width, height);

		if (color == EnemyColor.BLUE) {
			batch.draw(Assets.getFullGameObject("enemy_blue"), x, y
					+ floatOffset.y, width, height + 0.2f);
		} else if (color == EnemyColor.RED) {
			batch.draw(Assets.getFullGameObject("enemy_red"), x, y
					+ floatOffset.y, width, height + 0.2f);
		}
	}
}
