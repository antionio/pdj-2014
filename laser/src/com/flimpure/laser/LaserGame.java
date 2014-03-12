package com.flimpure.laser;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.flimpure.laser.assets.Assets;
import com.flimpure.laser.entity.Entity;
import com.flimpure.laser.screen.GameScreen;
import com.flimpure.laser.tween.Vector2Accessor;

public class LaserGame extends Game {

	@Override
	public void create() {

        Assets.loadGameData();

        Tween.setCombinedAttributesLimit(3);
//        Tween.registerAccessor(Entity.class, new EntityAccessor());
        Tween.registerAccessor(Vector2.class, new Vector2Accessor());
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
