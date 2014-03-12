package com.flimpure.laser.screen;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.flimpure.laser.LaserGame;
import com.flimpure.laser.assets.Assets;
import com.flimpure.laser.entity.*;
import com.flimpure.laser.level.Dungeon;
import com.flimpure.laser.entity.Direction;
import com.flimpure.laser.entity.Enemy;
import com.flimpure.laser.entity.EnemyColor;
import com.flimpure.laser.entity.Entity;
import com.flimpure.laser.entity.Player;

public class GameScreen extends Basic2DScreen {

	private final ScreenShake screenShake;
	public final Player player;
	public final Array<Entity> entities = new Array<Entity>();

    private final Dungeon dungeon;
    protected final Vector2 playerPos = new Vector2();
    protected final Vector2 cameraLerp = new Vector2();

	private ShapeRenderer sr;

	public GameScreen(LaserGame game) {
		super(game, 24, 16);
		camera.position.set(12f, 8f, 0f);
		camera.update();
		screenShake = new ScreenShake(camera);
		player = new Player(8f, 8f, 1f, 1f, this);
		entities.add(new Enemy(12f, 8f, EnemyColor.RED, this));
		entities.add(new Enemy(12f, 10f, EnemyColor.BLUE, this));

		sr = new ShapeRenderer();

        dungeon = new Dungeon();
        cameraLerp.set(player.x, player.y);
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

		for (int i = 0; i < entities.size; i++) {
			entities.get(i).update(fixedStep);
		}

        dungeon.update(fixedStep);

		screenShake.update(fixedStep);


        // update camera position
        playerPos.set(player.x, player.y);
        cameraLerp.lerp(playerPos, 5f * fixedStep);
        camera.position.set(cameraLerp.x, cameraLerp.y, 0f);
        camera.update();
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
					Vector3 v2 = new Vector3(Gdx.input.getX(),
							Gdx.input.getY(), 0.0f);
					camera.unproject(v2);

					float xd = v2.x - player.x();
					float yd = v2.y - player.y();
					Vector2 rel = new Vector2(xd, yd);

					// no targets, draw an infinite laser
					Vector2 beam = new Vector2(50.0f, 0.0f);
					beam.rotate(rel.angle());
					beam.add(player.x(), player.y());

					player.laserTarget.x = beam.x;
					player.laserTarget.y = beam.y;

                    if (!screenShake.active) {
                        screenShake.activate(0.1f, null);
                    }

                    Assets.getGameSound(Assets.SOUND_LASER).loop();
				} else {
					 Assets.getGameSound(Assets.SOUND_LASER).stop();
				}
			}
		}
	}

	/**
	 * *YOINK* http://stackoverflow.com/questions/13053061/circle-line-intersection-points
	 * @param pointA
	 * @param pointB
	 * @param center
	 * @param radius
	 * @return
	 */
	public static Array<Vector2> getCircleLineIntersectionPoint(Vector2 pointA,
			Vector2 pointB, Vector2 center, float radius) {
		float baX = pointB.x - pointA.x;
		float baY = pointB.y - pointA.y;
		float caX = center.x - pointA.x;
		float caY = center.y - pointA.y;

		float a = baX * baX + baY * baY;
		float bBy2 = baX * caX + baY * caY;
		float c = caX * caX + caY * caY - radius * radius;

		float pBy2 = bBy2 / a;
		float q = c / a;

		float disc = pBy2 * pBy2 - q;
		if (disc < 0) {
			return new Array<Vector2>();
		}
		// if disc == 0 ... dealt with later
		float tmpSqrt = (float) Math.sqrt(disc); // PRECISION LOSS
		float abScalingFactor1 = -pBy2 + tmpSqrt;
		float abScalingFactor2 = -pBy2 - tmpSqrt;

		Vector2 p1 = new Vector2(pointA.x - baX * abScalingFactor1, pointA.y
				- baY * abScalingFactor1);
		if (disc == 0) { // abScalingFactor1 == abScalingFactor2
			Array<Vector2> ar = new Array<Vector2>();
			ar.add(p1);
			return ar;
		}
		Vector2 p2 = new Vector2(pointA.x - baX * abScalingFactor2, pointA.y
				- baY * abScalingFactor2);
		Array<Vector2> ar = new Array<Vector2>();
		ar.add(p1);
		ar.add(p2);
		return ar;
	}

	@Override
	public void renderScreen(float delta) {
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();

        dungeon.render(delta, spriteBatch);

		spriteBatch.end();

		// shoot some laser!
		sr.setProjectionMatrix(camera.combined);
		if (Gdx.input.isButtonPressed(0)) {
			Vector2 v1 = new Vector2(player.x(), player.y());
			Vector2 v2 = player.laserTarget;

			// check if the beam hits anything
			sr.begin(ShapeType.Filled);
			sr.setColor(Color.RED);
			sr.rectLine(v1, v2, 0.25f);

			sr.end();
		}

		spriteBatch.begin();
		player.render(delta, spriteBatch);
		for (int i = 0; i < entities.size; i++) {
			entities.get(i).render(delta, spriteBatch);
		}
		spriteBatch.end();

		sr.begin(ShapeType.Line);
		for (Entity e : entities) {
			sr.circle(e.x, e.y, 0.5f, 10);
		}
		sr.end();
	}

	@Override
	public void show() {
		super.show();
		Gdx.input.setInputProcessor(this);
	}
}
//