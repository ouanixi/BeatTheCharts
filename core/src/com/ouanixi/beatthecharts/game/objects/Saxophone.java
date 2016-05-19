package com.ouanixi.beatthecharts.game.objects;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ouanixi.beatthecharts.game.Assets;

/**
 * Created by ouanixi on 15/04/16.
 */
public class Saxophone extends AbstractGameObject {
    private TextureRegion regCarrot;

    public Saxophone () {
        init();
    }

    private void init () {
        dimension.set(0.25f, 0.5f);

        regCarrot = Assets.instance.levelDecoration.carrot;

        // Set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
        origin.set(dimension.x / 2, dimension.y / 2);
    }
    public void render (SpriteBatch batch) {
        TextureRegion reg = null;

        reg = regCarrot;
        batch.draw(reg.getTexture(), position.x - origin.x, position.y - origin.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
    }
}
