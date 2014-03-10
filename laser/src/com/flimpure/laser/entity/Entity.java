package com.flimpure.laser.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.flimpure.laser.assets.Assets;

public abstract class Entity {

    protected float x,y,width, height;
    private final Rectangle bounds;
    private EntityState state;
    private Direction direction = Direction.DOWN;
    private final Vector2 accel = new Vector2(0f, 0f);
    private final Vector2 vel = new Vector2(0f, 0f);
    public float stateTime = 0f;
    public float pause = 0f;

    public static final float ACCEL_MAX = 0.005f;
    public static final float VEL_MAX = 0.05f;
    public static final float INERTIA = 5f;
    private static final float MIN_WALK_VELOCITY = 0.001f;
    protected static final float INVULNERABLE_TIME_MIN = 1.5f;
    protected static final float BLINK_TICK_MAX = 0.1f;

    protected Entity(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.bounds = new Rectangle(x, y, width, height);
        state = EntityState.IDLE;
    }

    public float getMaxVelocity() {
        return VEL_MAX;
    }

    public float getInertia() {
        return INERTIA;
    }

    public void render(float delta, SpriteBatch batch) {
        batch.draw(Assets.getGameObject("dummy"), x, y, width, height);
    }

    public void update(float fixedStep) {
        if (pause > 0f) {
            pause -= fixedStep;
        } else {
            tryMove();

            x += vel.x;
            y += vel.y;

            vel.add(accel);
            if (vel.x > getMaxVelocity()) {
                vel.x = getMaxVelocity();
            }
            if (vel.x < -getMaxVelocity()) {
                vel.x = -getMaxVelocity();
            }
            if (vel.y > getMaxVelocity()) {
                vel.y = getMaxVelocity();
            }
            if (vel.y < -getMaxVelocity()) {
                vel.y = -getMaxVelocity();
            }
            accel.scl(fixedStep);

            if (state == EntityState.WALKING) {
                vel.lerp(Vector2.Zero, getInertia() * fixedStep);
            } else {
                vel.scl(fixedStep);
            }

            stateTime += fixedStep;
        }
    }

    private void tryMove() {
    }

    public void moveWithAccel(Direction dir, float axisAmount) {
        float axis = axisAmount;

        if (axis > ACCEL_MAX) {
            axis = ACCEL_MAX;
        }
        if (dir == Direction.UP) {
            accel.y = axis;
        } else if (dir == Direction.DOWN) {
            accel.y = -axis;
        } else if (dir == Direction.LEFT) {
            accel.x = -axis;
        } else if (dir == Direction.RIGHT) {
            accel.x = axis;
        }
        direction = dir;
        state = EntityState.WALKING;
    }

    public boolean isAlive() {
        return state != EntityState.DEAD && state != EntityState.DYING;
    }
}
