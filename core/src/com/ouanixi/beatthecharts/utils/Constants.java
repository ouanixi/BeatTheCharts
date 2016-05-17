package com.ouanixi.beatthecharts.utils;

/**
 * Created by ouanixi on 11/04/16.
 */
public class Constants {
    // Visible game world is 5 meters wide
    public static final float VIEWPORT_WIDTH = 12.80f;
    // Visible game world is 5 meters tall
    public static final float VIEWPORT_HEIGHT = 7.20f;
    // GUI Width
    public static final float VIEWPORT_GUI_WIDTH = 800;
    // GUI Height
    public static final float VIEWPORT_GUI_HEIGHT = 400;

    // Location of description file for texture atlas
    public static final String TEXTURE_ATLAS_OBJECTS = "images/beatthecharts.pack";

    // Location of image file for level 01
    public static final String LEVEL_01 = "levels/level-01.png";

    // Amount of extra lives at level start
    public static final int LIVES_START = 3;

    // Duration of feather power-up in seconds
    public static final float ITEM_FEATHER_POWERUP_DURATION = 9;

    // Delay after game over
    public static final float TIME_DELAY_GAME_OVER = 3;


    public static final String TEXTURE_ATLAS_UI =
            "images/beatthecharts-ui.pack";
    public static final String TEXTURE_ATLAS_LIBGDX_UI =
            "images/uiskin.atlas";
    // Location of description file for skins
    public static final String SKIN_LIBGDX_UI =
            "images/uiskin.json";
    public static final String SKIN_BEATTHECHARTS_UI =
            "images/beatthecharts-ui.json";


    public static final String PREFERENCES = "beatthecharts.prefs";

    // Number of carrots to spawn
    public static final int CARROTS_SPAWN_MAX = 100;

    // Spawn radius for carrots
    public static final float CARROTS_SPAWN_RADIUS = 3.5f;

    // Delay after game finished
    public static final float TIME_DELAY_GAME_FINISHED = 6;

    // Angle of rotation for dead zone (no movement)
    public static final float ACCEL_ANGLE_DEAD_ZONE = 5.0f;

    // Max angle of rotation needed to gain max movement velocity
    public static final float ACCEL_MAX_ANGLE_MAX_MOVEMENT = 20.0f;


}