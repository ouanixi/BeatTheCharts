package com.ouanixi.beatthecharts.transitions;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by ouanixi on 14/04/16.
 */
public interface ScreenTransition {

    public float getDuration();
    public void render (SpriteBatch batch, Texture currScreen, Texture nextScreen, float alpha);
}
