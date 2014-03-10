package com.flimpure.laser.tools;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.flimpure.laser.assets.Assets;

public class PackTextures {

	private static final String UNPROCESSED_FOLDER = "C:\\Users\\Antti\\Dropbox\\ANewShinyCompany\\laser-unprocessed";
	private static final String PROCESSED_FOLDER = "C:\\Users\\Antti\\Documents\\GitHub\\pdj-2014\\laser-android\\assets\\data";

	public static void main(String[] args) {
		final TexturePacker.Settings settings = new TexturePacker.Settings();
		settings.maxWidth = 2048;
		settings.maxHeight = 2048;
		settings.paddingX = 4;
		settings.paddingY = 4;
		settings.bleed = true;
		settings.edgePadding = true;
		settings.pot = true;
		TexturePacker.process(settings, UNPROCESSED_FOLDER, PROCESSED_FOLDER,
                Assets.ATLAS_FILE_OBJECTS_ALL);
	}
}
