package com.flimpure.laser.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.flimpure.laser.assets.Assets;

public class Enemy extends Entity {

    private EnemyColor color;

    public Enemy(float x, float y, EnemyColor color) {
        super(x, y, 1f, 1f);
        this.color = color;
    }


    @Override
    public void update(float fixedStep) {
        super.update(fixedStep);


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
