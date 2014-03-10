package com.flimpure.laser.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.flimpure.laser.LaserGame;
import com.flimpure.laser.assets.Assets;
import com.flimpure.laser.entity.Direction;
import com.flimpure.laser.entity.Entity;
import com.flimpure.laser.entity.Player;

public class GameScreen extends Basic2DScreen {

	private final ScreenShake screenShake;
	private final Player player;

	private ShapeRenderer sr;

	public GameScreen(LaserGame game) {
		super(game, 24, 16);
		camera.position.set(12f, 8f, 0f);
		camera.update();
		screenShake = new ScreenShake(camera);
		player = new Player(8f, 8f, 1f, 1f);

		sr = new ShapeRenderer();
	}

	@Override
	public void dispose() {
		super.dispose();
		sr.dispose();
	}

	@Override
	protected void updateScreen(float fixedStep) {
		processKeys();

		player.update(fixedStep);

		screenShake.update(fixedStep);
	}

	private void processKeys() {
		{
			if (!paused && player.isAlive()) {
				// process player movement keys
				if (Gdx.input.isKeyPressed(Input.Keys.W)) {
					player.moveWithAccel(Direction.UP, Entity.ACCEL_MAX);
				}
				if (Gdx.input.isKeyPressed(Input.Keys.S)) {
					player.moveWithAccel(Direction.DOWN, Entity.ACCEL_MAX);
				}
				if (Gdx.input.isKeyPressed(Input.Keys.D)) {
					player.moveWithAccel(Direction.RIGHT, Entity.ACCEL_MAX);
				}
				if (Gdx.input.isKeyPressed(Input.Keys.A)) {
					player.moveWithAccel(Direction.LEFT, Entity.ACCEL_MAX);
				}

				if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
					camera.zoom += 0.1f;
					camera.update();
				}
				if (Gdx.input.isKeyPressed(Input.Keys.PLUS)) {
					camera.zoom -= 0.1f;
					camera.update();
				}
				

				if (Gdx.input.isButtonPressed(0)) {
					// intersect laser with game objects
					// update the laser target
					Vector3 v2 = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f);
					camera.unproject(v2);

					player.laserTarget.x = v2.x;
					player.laserTarget.y = v2.y;
				}
			}
		}
	}

	@Override
	public void renderScreen(float delta) {
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();

		spriteBatch.draw(Assets.getFullGameObject("background-temporary"), 0,
				0, 24, 16);

		spriteBatch.end();

		// shoot some laser!
		if (Gdx.input.isButtonPressed(0)) {
			Vector2 v1 = new Vector2(player.x() + 0.5f, player.y() + 0.5f);
			Vector2 v2 = player.laserTarget;
			
			// check if the beam hits anything
			sr.setProjectionMatrix(camera.combined);
			sr.setColor(Color.RED);
			sr.begin(ShapeType.Filled);
			sr.rectLine(v1, v2, 0.25f);
			sr.rectLine(v2, Vector2.Zero, 0.25f);

			sr.end();
		}
		

		spriteBatch.begin();
		player.render(delta, spriteBatch);
		spriteBatch.end();

	}

	@Override
	public void show() {
		super.show();
		Gdx.input.setInputProcessor(this);
	}
}
