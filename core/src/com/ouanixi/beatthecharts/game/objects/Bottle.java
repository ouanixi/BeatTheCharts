package com.ouanixi.beatthecharts.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ouanixi.beatthecharts.game.Assets;

/**
 * Created by ouanixi on 12/04/16.
 */
public class Bottle extends AbstractGameObject {

    private TextureRegion regBottle;
    public boolean collected;

    public Bottle(){
        init();
    }

    private void init() {
        dimension.set(0.8f, 0.5f);
        regBottle = Assets.instance.bottle.bottle;

        bounds.set(0,0, dimension.x, dimension.y);
        collected = false;
    }

    @Override
    public void render(SpriteBatch batch) {
        if(collected) return;

        TextureRegion reg = null;
        reg = regBottle;
        batch.draw(reg.getTexture(),
                position.x, position.y,
                origin.x, origin.y,
                dimension.x, dimension.y,
                scale.x, scale.y,
                rotation,
                reg.getRegionX(), reg.getRegionY(),
                reg.getRegionWidth(), reg.getRegionHeight(),
                false, false);
    }

    public int getScore(){
        return 100;
    }
}
