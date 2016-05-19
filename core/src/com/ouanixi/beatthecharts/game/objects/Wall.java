package com.ouanixi.beatthecharts.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.ouanixi.beatthecharts.game.Assets;

/**
 * Created by ouanixi on 11/04/16.
 */
public class Wall extends AbstractGameObject {
    private TextureRegion regMiddle;
    private TextureRegion regEdge;

    private final float FLOAT_CYCLE_TIME = 2.0f;
    private final float FLOAT_AMPLITUDE = 4.0f;
    private float floatCycleTimeLeft;
    private boolean floatingDownwards;
    private Vector2 floatTargetPosition;

    private int length;

    public Wall() {
        init();
    }

    private void init() {
        dimension.set(1, 1.5f);
        regEdge = Assets.instance.wall.edge;
        regMiddle = Assets.instance.wall.middle;

        setLength(1);

        floatingDownwards = false;
        floatCycleTimeLeft = MathUtils.random(0, FLOAT_CYCLE_TIME * 0.5f);
        floatTargetPosition = null;
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion reg = null;

        float relX = 0;
        float relY = 0;

        // Draw left edge
        reg = regEdge;
        relX -= dimension.x / 4;
        batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y, dimension.x / 4, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);

        // Draw middle
        relX = 0;
        reg = regMiddle;
        for (int i = 0; i < length; i++) {
            batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
            relX += dimension.x;
        }

        // Draw right edge
        reg = regEdge;
        batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x + dimension.x / 8, origin.y, dimension.x / 4, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), true, false);
    }

    public void setLength(int length) {
        this.length = length;

        bounds.set(0, 0, dimension.x * length, dimension.y);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        floatCycleTimeLeft -= deltaTime;
        if (floatCycleTimeLeft <= 0) {
            floatCycleTimeLeft = FLOAT_CYCLE_TIME;
            floatingDownwards = !floatingDownwards;
            body.setLinearVelocity(0, FLOAT_AMPLITUDE * (floatingDownwards ? -1 : 1));
        } else {
            body.setLinearVelocity(body.getLinearVelocity().scl(0.98f));

        }
    }

    public void increaseLength(int amount) {
        setLength(length + amount);
    }
}
