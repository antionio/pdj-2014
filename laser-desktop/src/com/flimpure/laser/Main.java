package com.flimpure.laser;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "laser";
		cfg.width = 960;
		cfg.height = 600;
        cfg.vSyncEnabled = true;
        cfg.foregroundFPS = 5000;
		
		new LwjglApplication(new LaserGame(), cfg);
	}
}
