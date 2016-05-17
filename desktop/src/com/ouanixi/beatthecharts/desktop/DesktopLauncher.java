package com.ouanixi.beatthecharts.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.ouanixi.beatthecharts.BeatTheChartsMain;

public class DesktopLauncher {
	public static boolean rebuildAtlas = false;
	private static boolean drawDebugOutline = false;


	public static void main (String[] arg) {
		if(rebuildAtlas){
			TexturePacker.Settings settings = new TexturePacker.Settings();
			settings.debug = drawDebugOutline;
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.atlasExtension = ".pack";

			TexturePacker.process(settings, "assets-raw/images",
					"../assets/images/", "beatthecharts");

			TexturePacker.process(settings, "assets-raw/images-ui",
					"../assets/images/", "beatthecharts-ui" );
		}


		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new BeatTheChartsMain(), config);
	}
}