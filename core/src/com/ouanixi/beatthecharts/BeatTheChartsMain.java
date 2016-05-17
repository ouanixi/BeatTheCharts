package com.ouanixi.beatthecharts;



import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Interpolation;
import com.ouanixi.beatthecharts.game.Assets;
import com.ouanixi.beatthecharts.screens.DirectedGame;
import com.ouanixi.beatthecharts.screens.MenuScreen;
import com.ouanixi.beatthecharts.transitions.ScreenTransition;
import com.ouanixi.beatthecharts.transitions.ScreenTransitionSlice;
import com.ouanixi.beatthecharts.utils.AudioManager;
import com.ouanixi.beatthecharts.utils.GamePreferences;


public class BeatTheChartsMain extends DirectedGame {
    private static final String TAG = BeatTheChartsMain.class.getName();


    @Override
    public void create () {
        // Set Libgdx log level
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        // Load assets
        Assets.instance.init(new AssetManager());

        // Load preferences for audio settings and start playing music
        GamePreferences.instance.load();
        AudioManager.instance.play(Assets.instance.music.song01);

        // Start game at menu screen
        ScreenTransition transition = ScreenTransitionSlice.init(2, ScreenTransitionSlice.UP_DOWN, 10, Interpolation.pow5Out);
        setScreen(new MenuScreen(this), transition);
    }


}


