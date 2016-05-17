package com.ouanixi.beatthecharts.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.ouanixi.beatthecharts.game.objects.*;

/**
 * Created by ouanixi on 11/04/16.
 */
public class Level {
    public static final String TAG = Level.class.getName();
    public Kenny kenny;
    public Array<Bottle> bottles;
    public Array<Paper> papers;
    public Array<Carrot> carrots;
    public Goal goal;
    // objects
    public Array<Wall> walls;
    // decoration
    public Clouds clouds;
    public Buildings mountains;
    public WaterOverlay waterOverlay;

    public Level(String filename) {
        init(filename);
    }

    private void init(String filename) {
        // player character
        kenny = null;
        // objects
        walls = new Array<Wall>();
        bottles = new Array<Bottle>();
        papers = new Array<Paper>();
        carrots = new Array<Carrot>();

        // load image file that represents the level data
        Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
        // scan pixels from top-left to bottom-right
        int lastPixel = -1;
        for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
            for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
                AbstractGameObject obj;
                float offsetHeight;
                // height grows from bottom to top
                float baseHeight = pixmap.getHeight() - pixelY;
                // get color of current pixel as 32-bit RGBA value
                int currentPixel = pixmap.getPixel(pixelX, pixelY);
                // find matching color value to identify block type at (x,y)
                // point and create the corresponding game object if there is
                // a match
                // empty space
                if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {
                    // do nothing
                }
                // wall
                else if (BLOCK_TYPE.ROCK.sameColor(currentPixel)) {
                    if (lastPixel != currentPixel) {
                        obj = new Wall();
                        float heightIncreaseFactor = 0.25f;
                        offsetHeight = -2.5f;
                        obj.position.set(pixelX, baseHeight * obj.dimension.y * heightIncreaseFactor + offsetHeight);
                        walls.add((Wall) obj);
                    } else {
                        walls.get(walls.size - 1).increaseLength(1);
                    }
                }
                // player spawn point
                else if (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) {
                    obj = new Kenny();
                    offsetHeight = -3.0f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    kenny = (Kenny) obj;
                }
                // feather
                else if (BLOCK_TYPE.ITEM_FEATHER.sameColor(currentPixel)) {
                    obj = new Paper();
                    offsetHeight = -1.5f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    papers.add((Paper) obj);
                }
                // gold coin
                else if (BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel)) {
                    obj = new Bottle();
                    offsetHeight = -1.5f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    bottles.add((Bottle) obj);
                }
                // goal
                else if (BLOCK_TYPE.GOAL.sameColor(currentPixel)) {
                    obj = new Goal();
                    offsetHeight = -7.0f;
                    obj.position.set(pixelX, baseHeight + offsetHeight);
                    goal = (Goal)obj;
                }
                // unknown object/pixel color
                else {
                    int r = 0xff & (currentPixel >>> 24); //red color channel
                    int g = 0xff & (currentPixel >>> 16); //green color channel
                    int b = 0xff & (currentPixel >>> 8); //blue color channel
                    int a = 0xff & currentPixel; //alpha channel
                    Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<" + pixelY + ">: r<" + r + "> g<" + g + "> b<" + b + "> a<" + a + ">");
                }
                lastPixel = currentPixel;
            }
        }
        // decoration
        clouds = new Clouds(pixmap.getWidth());
        clouds.position.set(0, 2);
        mountains = new Buildings
                (pixmap.getWidth());
        mountains.position.set(-1, -1);
        waterOverlay = new WaterOverlay(pixmap.getWidth());
        waterOverlay.position.set(0, -3.75f);
        // free memory
        pixmap.dispose();
        Gdx.app.debug(TAG, "level '" + filename + "' loaded");
    }

    public void render(SpriteBatch batch) {
        // Draw Buildings

        mountains.render(batch);

        // Draw Walls
        for (Wall wall : walls) {
            wall.render(batch);
        }

        //Draw goal
        goal.render(batch);
        // Draw Gold Coins
        for (Bottle goldCoin : bottles)
            goldCoin.render(batch);
        // Draw Papers
        for (Paper feather : papers)
            feather.render(batch);

        // draw carrots
        for (Carrot carrot: carrots)
                carrot.render(batch);

        // Draw Player Character
        kenny.render(batch);
        // Draw Water Overlay
        waterOverlay.render(batch);
        // Draw Clouds
        clouds.render(batch);
    }

    public void update(float deltaTime) {
        kenny.update(deltaTime);
        for (Wall wall : walls)
            wall.update(deltaTime);
        for (Bottle goldCoin : bottles)
            goldCoin.update(deltaTime);
        for (Paper feather : papers)
            feather.update(deltaTime);
        for (Carrot carrot : carrots)
            carrot.update(deltaTime);
        clouds.update(deltaTime);
    }

    public enum BLOCK_TYPE {
        EMPTY(0, 0, 0), // black
        ROCK(0, 255, 0), // green
        PLAYER_SPAWNPOINT(255, 255, 255), // white
        ITEM_FEATHER(255, 0, 255), // purple
        GOAL(255, 0, 0), // red
        ITEM_GOLD_COIN(255, 255, 0); // yellow

        private int color;

        BLOCK_TYPE(int r, int g, int b) {
            color = r << 24 | g << 16 | b << 8 | 0xff;
        }

        public boolean sameColor(int color) {
            return this.color == color;
        }

        public int getColor() {
            return color;
        }
    }
}
