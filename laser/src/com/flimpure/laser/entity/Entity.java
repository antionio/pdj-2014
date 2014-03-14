package com.flimpure.laser.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.flimpure.laser.assets.Assets;
import com.flimpure.laser.level.tiles.Tile;
import com.flimpure.laser.screen.GameScreen;

public abstract class Entity {

    protected GameScreen gameScreen;
    public float x,y,width, height;
    protected EntityState state;
    protected Direction direction = Direction.DOWN;
    public final Rectangle bounds = new Rectangle();
    public final Vector2 accel = new Vector2(0f, 0f);
    protected final Vector2 vel = new Vector2(0f, 0f);
    public float stateTime = 0f;
    public float pause = 0f;

    public static final float ACCEL_MAX = 0.005f;
    public static final float VEL_MAX = 0.05f;
    public static final float INERTIA = 5f;
    private static final float MIN_WALK_VELOCITY = 0.001f;
    protected static final float INVULNERABLE_TIME_MIN = 1.5f;
    protected static final float BLINK_TICK_MAX = 0.1f;

    private float health = 100;

    protected Entity(float x, float y, float width, float height, GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        state = EntityState.IDLE;

        bounds.set(x, y, width - 0.2f, height - 0.2f);
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
        	
        	if (!isAlive()) {
        		return;
        	}
        	
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
        bounds.x += vel.x;
        fetchCollidableRects(gameScreen.dungeon.tileMap);
        for (int i = 0; i < r.length; i++) {
            Rectangle rect = r[i];
            if (bounds.overlaps(rect)) {
                if (vel.x < 0)
                    bounds.x = rect.x + rect.width + 0.01f;
                else
                    bounds.x = rect.x - bounds.width - 0.01f;
                vel.x = 0;
            }
        }

        bounds.y += vel.y;
        fetchCollidableRects(gameScreen.dungeon.tileMap);
        for (int i = 0; i < r.length; i++) {
            Rectangle rect = r[i];
            if (bounds.overlaps(rect)) {
                if (vel.y < 0) {
                    bounds.y = rect.y + rect.height + 0.01f;
                } else
                    bounds.y = rect.y - bounds.height - 0.01f;
                vel.y = 0;
            }
        }

        x = bounds.x + width / 2 - 0.1f;
        y = bounds.y + height / 2 - 0.1f;
    }

    private Rectangle[] r = { new Rectangle(), new Rectangle(), new Rectangle(), new Rectangle() };
    protected final Vector2[] tiles = new Vector2[] { new Vector2(), new Vector2(), new Vector2(), new Vector2() };

    protected void fetchCollidableRects(int[][] tiles) {
        try {
            int p1x = (int) bounds.x;
            int p1y = (int) Math.floor(bounds.y);
            int p2x = (int) (bounds.x + bounds.width);
            int p2y = (int) Math.floor(bounds.y);
            int p3x = (int) (bounds.x + bounds.width);
            int p3y = (int) (bounds.y + bounds.height);
            int p4x = (int) bounds.x;
            int p4y = (int) (bounds.y + bounds.height);

            Tile tile1 = Tile.tiles[tiles[p1x][p1y]];
            Tile tile2 = Tile.tiles[tiles[p2x][p2y]];
            Tile tile3 = Tile.tiles[tiles[p3x][p3y]];
            Tile tile4 = Tile.tiles[tiles[p4x][p4y]];

            this.tiles[0].set(p1x, p1y);
            this.tiles[1].set(p2x, p2y);
            this.tiles[2].set(p3x, p3y);
            this.tiles[3].set(p4x, p4y);

            if (tile1.isCollidable())
                r[0].set(p1x, p1y, 1, 1);
            else
                r[0].set(-1, -1, 0, 0);
            if (tile2.isCollidable())
                r[1].set(p2x, p2y, 1, 1);
            else
                r[1].set(-1, -1, 0, 0);
            if (tile3.isCollidable())
                r[2].set(p3x, p3y, 1, 1);
            else
                r[2].set(-1, -1, 0, 0);
            if (tile4.isCollidable())
                r[3].set(p4x, p4y, 1, 1);
            else
                r[3].set(-1, -1, 0, 0);
        } catch (ArrayIndexOutOfBoundsException e) {
            Gdx.app.log("Creature", "Player went off screen");
        }

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
    
    protected Vector2 position = new Vector2();
    
    public Vector2 getPosition() {
    	position.x = x;
    	position.y = y;
    	return position;
    }

    public void takeDamage(float damage) {
        health -= damage;
        if (health <= 0f) {
            state = EntityState.DYING;
        }
    }
    
    public void onHit(Player player) {
    }

}
