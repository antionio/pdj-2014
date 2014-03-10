package com.flimpure.laser.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.flimpure.laser.LaserGame;
import com.flimpure.laser.assets.Assets;
import com.flimpure.laser.entity.Direction;
import com.flimpure.laser.entity.Entity;
import com.flimpure.laser.entity.Player;

public class GameScreen extends Basic2DScreen {

    private final ScreenShake screenShake;
    private final Player player;

    public GameScreen(LaserGame game) {
        super(game, 24, 16);
        camera.position.set(12f, 8f, 0f);
        camera.update();
        screenShake = new ScreenShake(camera);
        player = new Player(8f,8f,1f,1f);
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
            }
        }
    }

    @Override
    public void renderScreen(float delta) {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        spriteBatch.draw(Assets.getFullGameObject("background-temporary"), 0, 0, 24, 16);

        player.render(delta, spriteBatch);
        spriteBatch.end();
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(this);
    }
}
