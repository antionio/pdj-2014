package com.flimpure.laser.level;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.flimpure.laser.entity.Entity;
import com.flimpure.laser.level.tiles.Tile;
import com.flimpure.laser.screen.GameScreen;

public class Dungeon {

    private final GameScreen gameScreen;
    private final Array<Entity> entities = new Array<Entity>();
    private final int width, height;
    public final int[][] tileMap;


    public Dungeon(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        width = 96;
        height = 48;
        tileMap = DungeonGenerator.getTileMap(width, height);

//        Light light = new ConeLight(rayHandler, 128, Color.WHITE, 60, 0, 34,
//                270, 100);

    }

    public void update(float fixedStep) {
        for (int i = 0; i < entities.size; i++) {
            entities.get(i).update(fixedStep);
        }
    }

    public void render(float delta, SpriteBatch batch) {
        // draw the world
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
//                if (x < playerRight && x > playerLeft && y < playerUp && y > playerDown) {
                    Tile.tiles[tileMap[x][y]].render(delta, batch, x, y);
//                }
            }
        }
    }

    public void dispose() {
    }

}
