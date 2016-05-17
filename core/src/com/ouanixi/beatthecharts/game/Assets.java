package com.ouanixi.beatthecharts.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.ouanixi.beatthecharts.utils.Constants;

/**
 * Created by ouanixi on 11/04/16.
 */
public class Assets implements Disposable, AssetErrorListener {
    public static final String TAG = Assets.class.getName();

    public AssetKenny kenny;
    public AssetWall wall;
    public AssetBottle bottle;
    public AssetPaper paper;
    public AssetLevelDecoration levelDecoration;
    public AssetFonts fonts;
    public AssetEnemy enemy;

    public AssetSounds sounds;
    public AssetMusic music;

    public static final Assets instance = new Assets();

    private AssetManager assetManager;


    // Making this private to reflect the singleton pattern]
    // and prevent instantiation from anywhere apart from this class.
    private Assets(){};

    public void init(AssetManager assetManager){
        this.assetManager = assetManager;
        assetManager.setErrorListener(this);

        // loat the atlas file
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);

        //loads sounds
        assetManager.load("sounds/jump.wav", Sound.class);
        assetManager.load("sounds/jump_with_feather.wav", Sound.class);
        assetManager.load("sounds/pickup_coin.wav", Sound.class);
        assetManager.load("sounds/pickup_feather.wav", Sound.class);
        assetManager.load("sounds/live_lost.wav", Sound.class);

        // loads music
        assetManager.load("music/keith303_-_brand_new_highscore.mp3", Music.class);

        assetManager.finishLoading(); // since the load doesn't actually start the process.
        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
        for(String a : assetManager.getAssetNames()){
            Gdx.app.debug(TAG, "asset: " + a);
        }

        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);

        // PIXEL SMOOTHING
        for (Texture t : atlas.getTextures()){
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        fonts = new AssetFonts();
        kenny = new AssetKenny(atlas);
        wall = new AssetWall(atlas);
        bottle = new AssetBottle(atlas);
        paper = new AssetPaper(atlas);
        levelDecoration = new AssetLevelDecoration(atlas);
        sounds = new AssetSounds(assetManager);
        music = new AssetMusic(assetManager);
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '"+ asset + "'", (Exception)throwable);
    }


    @Override
    public void dispose() {
        assetManager.dispose();
        fonts.defaultNormal.dispose();
        fonts.defaultSmall.dispose();
        fonts.defaultBig.dispose();
    }


    public class AssetKenny{
        public final AtlasRegion kenny;
        public final Animation animIdle;
        public final Animation animRunning;
        public final Animation animJumpRising;
        public final Animation animJumpLanding;

        public AssetKenny(TextureAtlas atlas){
            kenny = atlas.findRegion("kenny");

            Array<AtlasRegion> regions = null;
            AtlasRegion region = null;

            // Animation: Kenny idle
            regions = atlas.findRegions("anim_kenny_idle");
            animIdle = new Animation(1.0f / 10.0f, regions, Animation.PlayMode.LOOP);

            // Animation: Kenny running
            regions = atlas.findRegions("anim_kenny_running");
            animRunning = new Animation(1.0f / 10.0f, regions, Animation.PlayMode.LOOP);

            // Animation: Kenny jumpRising
            regions = atlas.findRegions("kenny_jump_rising");
            animJumpRising = new Animation(0, regions.get(0));

            // Animation: Kenny jumpLanding
            regions = atlas.findRegions("kenny_jump_falling");
            animJumpLanding = new Animation(0, regions.get(0));



        }
    }

    public class AssetEnemy{
        // TODO: Implement enemy class
        public final AtlasRegion enemy;
        public AssetEnemy(TextureAtlas atlas){
            enemy = atlas.findRegion("item_bottle");
        }
    }

    public class AssetWall{
        public final AtlasRegion middle;
        public final AtlasRegion edge;


        public AssetWall(TextureAtlas atlas){
            middle = atlas.findRegion("rock_middle");
            edge = atlas.findRegion("rock_edge");

        }
    }

    public class AssetBottle{
        public final AtlasRegion bottle;

        public AssetBottle(TextureAtlas atlas){
            bottle = atlas.findRegion("item_bottle");
        }
    }

    public class AssetPaper{
        public final AtlasRegion paper;

        public AssetPaper(TextureAtlas atlas){
            paper = atlas.findRegion("item_paper");
        }
    }

    public class AssetLevelDecoration {
        public final AtlasRegion cloud01;
        public final AtlasRegion cloud02;
        public final AtlasRegion cloud03;
        public final AtlasRegion buildingLeft;
        public final AtlasRegion buildingRight;
        public final AtlasRegion waterOverlay;

        public final AtlasRegion carrot;
        public final AtlasRegion goal;

        public AssetLevelDecoration (TextureAtlas atlas) {
            cloud01 = atlas.findRegion("cloud01");
            cloud02 = atlas.findRegion("cloud02");
            cloud03 = atlas.findRegion("cloud03");
            buildingLeft = atlas.findRegion("mountain_left");
            buildingRight = atlas.findRegion("mountain_right");
            waterOverlay = atlas.findRegion("water_overlay");
            carrot = atlas.findRegion("carrot");
            goal = atlas.findRegion("goal");
        }
    }

    public class AssetFonts{
        public final BitmapFont defaultSmall;
        public final BitmapFont defaultNormal;
        public final BitmapFont defaultBig;

        public AssetFonts(){
            defaultSmall = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
            defaultNormal = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
            defaultBig = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);

            defaultSmall.getData().setScale(0.75f);
            defaultNormal.getData().setScale(1.0f);
            defaultBig.getData().setScale(2.0f);

            defaultSmall.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            defaultNormal.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            defaultBig.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
    }

    public class AssetSounds {
        public final Sound jump;
        public final Sound jumpWithPaper;
        public final Sound pickupBottle;
        public final Sound pickupPaper;
        public final Sound liveLost;

        public AssetSounds(AssetManager am){
            jump = am.get("sounds/jump.wav", Sound.class);
            jumpWithPaper = am.get("sounds/jump_with_feather.wav", Sound.class);
            pickupBottle = am.get("sounds/pickup_coin.wav", Sound.class);
            pickupPaper = am.get("sounds/pickup_feather.wav", Sound.class);
            liveLost = am.get("sounds/live_lost.wav", Sound.class);
        }

    }

    public class AssetMusic {
        public final Music song01;

        public AssetMusic(AssetManager am){
            song01 = am.get("music/keith303_-_brand_new_highscore.mp3", Music.class);
        }
    }

}
