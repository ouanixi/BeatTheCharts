package com.ouanixi.beatthecharts.game.objects;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ouanixi.beatthecharts.game.Assets;


/**
 * Created by ouanixi on 12/04/16.
 */
public class Paper extends AbstractGameObject {

    private TextureRegion regPaper;
    public boolean collected;

    public Paper(){
        init();
    }

    private void init() {
        dimension.set(0.5f, 0.5f);
        regPaper = Assets.instance.paper.paper;
        bounds.set(0,0,dimension.x, dimension.y);
        collected = false;
    }

    @Override
    public void render(SpriteBatch batch) {
        if(collected) return;

        TextureRegion reg = null;
        reg = regPaper;
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

    public int getScore() {
        return 250;
    }
}
