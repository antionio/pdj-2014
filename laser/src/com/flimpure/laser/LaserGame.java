package com.flimpure.laser;

import com.badlogic.gdx.Game;
import com.flimpure.laser.assets.Assets;
import com.flimpure.laser.screen.GameScreen;

public class LaserGame extends Game {

	@Override
	public void create() {

        Assets.loadGameData();

//        Tween.setCombinedAttributesLimit(3);
//        Tween.registerAccessor(Entity.class, new EntityAccessor());
//        Tween.registerAccessor(Vector2.class, new Vector2Accessor());
//        Tween.registerAccessor(Vector3.class, new Vector3Accessor());

        //setScreen(new MenuScreen(this));
        setScreen(new GameScreen(this));
	}

    @Override
    public void dispose() {
        super.dispose();
        Assets.clear();
    }

    public boolean isDebug() {
        return true;
    }
}
