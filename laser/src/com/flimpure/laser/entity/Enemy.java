package com.flimpure.laser.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.flimpure.laser.assets.Assets;
import com.flimpure.laser.screen.GameScreen;

public class Enemy extends Entity {

    private EnemyColor color;

    public Enemy(float x, float y, EnemyColor color, GameScreen gameScreen) {
        super(x, y, 1f, 1f, gameScreen);
        this.color = color;
    }


    private final Vector2 playerPos = new Vector2();
    private final Vector2 mummyPos = new Vector2();

    @Override
    public float getMaxVelocity() {
        return 2f;
    }

    @Override
    public void update(float fixedStep) {
        super.update(fixedStep);

        playerPos.set(gameScreen.player.x, gameScreen.player.y);
        mummyPos.set(x, y);

        accel.set(playerPos.sub(mummyPos));
    }

    @Override
    public void render(float delta, SpriteBatch batch) {
        batch.draw(Assets.getFullGameObject("enemy_shadow"), x, y - 0.5f, width, height);

        if (color == EnemyColor.BLUE) {
            batch.draw(Assets.getFullGameObject("enemy_blue"), x, y, width, height + 0.2f);
        } else if (color == EnemyColor.RED) {
            batch.draw(Assets.getFullGameObject("enemy_red"), x, y, width, height + 0.2f);
        }
    }
}
