package com.ouanixi.beatthecharts.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.ouanixi.beatthecharts.game.objects.*;
import com.ouanixi.beatthecharts.screens.DirectedGame;
import com.ouanixi.beatthecharts.screens.MenuScreen;
import com.ouanixi.beatthecharts.transitions.ScreenTransition;
import com.ouanixi.beatthecharts.transitions.ScreenTransitionSlide;
import com.ouanixi.beatthecharts.utils.AudioManager;
import com.ouanixi.beatthecharts.utils.CameraController;
import com.ouanixi.beatthecharts.utils.Constants;

import java.util.Stack;


/**
 * Created by ouanixi on 11/04/16.
 */
public class WorldController extends InputAdapter implements Disposable{
    private static final String TAG = WorldController.class.getName();

    private boolean goalReached;
    public World b2world;

    private DirectedGame game;
    public float livesVisual;
    public float scoreVisual;

    public CameraController cameraController;
    public Level level;
    public int lives;
    public int score;
    public final Stack<String> stages = new Stack();

    private float timeLeftGameOverDelay;

    // rectangles for collision detection
    private Rectangle r1 = new Rectangle();
    private Rectangle r2 = new Rectangle();

    private boolean accelerometerAvailable;


    public WorldController(DirectedGame game) {
        this.game = game;
        init();
    }

    private void init() {
        // Adding game levels
        stages.add("final");
        stages.add(Constants.LEVEL_03);
        stages.add(Constants.LEVEL_02);
        stages.add(Constants.LEVEL_01);


        accelerometerAvailable = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
        cameraController = new CameraController();
        lives = Constants.LIVES_START;
        livesVisual = lives;
        timeLeftGameOverDelay = 0;
        initLevel(stages.pop());
    }

    private void initLevel (String lvl) {
        score = 0;
        scoreVisual = score;
        if (lvl.equals("final")) backToMenu();
        else {
            level = new Level(lvl);
            cameraController.setTarget(level.kenny);
            initPhysics();
        }
    }







    public void update (float deltaTime) {
        if (goalReached) {
            goalReached = false;
            initLevel(stages.pop());
        }
        if (isGameOver()) {
            timeLeftGameOverDelay -= deltaTime;
            if (timeLeftGameOverDelay< 0) backToMenu();
        } else {
            handleInputGame(deltaTime);
        }
        level.update(deltaTime);
        testCollisions();
        b2world.step(deltaTime, 8, 3);
        cameraController.update(deltaTime);
        if (!isGameOver() &&isPlayerInWater()) {
            AudioManager.instance.play(Assets.instance.sounds.liveLost);
            lives--;
            if (isGameOver())
                timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
            else
                initLevel(stages.peek());
        }

        if(livesVisual > lives)
            livesVisual = Math.max(lives, livesVisual - 1 * deltaTime);
        if(scoreVisual < score)
            scoreVisual = Math.min(score, scoreVisual + 250 * deltaTime);
    }







    @Override
    public boolean keyUp (int keycode) {
        // Reset game world
        if (keycode == Input.Keys.R) {
            init();
            Gdx.app.debug(TAG, "Game world resetted");
        }
        else if (keycode == Input.Keys.ENTER){
            cameraController.setTarget(cameraController.hasTarget() ? null: level.kenny);
            Gdx.app.debug(TAG, "Camera follow enabled: " + cameraController.hasTarget());
        }
        else if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK){
            backToMenu();
        }
        return false;
    }

    private void handleInputGame (float deltaTime) {
        if (cameraController.hasTarget(level.kenny)) {
            // Player Movement
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {

                level.kenny.velocity.x = -level.kenny.terminalVelocity.x;
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {

                level.kenny.velocity.x = level.kenny.terminalVelocity.x;
            } else {
                //Use accelerometer for movement if available
                if (accelerometerAvailable) {
                    // normalize accelerometer values from [-10, 10] to [-1, 1]// which translate to rotations of [-90, 90] degrees
                    float amount = Gdx.input.getAccelerometerY() / 10.0f;
                    amount *= 90.0f;
                    // is angle of rotation inside dead zone?
                    if (Math.abs(amount) <Constants.ACCEL_ANGLE_DEAD_ZONE) {
                        amount = 0;
                    } else {
                        // use the defined max angle of rotation instead of// the full 90 degrees for maximum velocity
                        amount /= Constants.ACCEL_MAX_ANGLE_MAX_MOVEMENT;
                    }
                    level.kenny.velocity.x = level.kenny.terminalVelocity.x * amount;
                }
//                // Execute auto-forward movement on non-desktop platform
//                if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
//                    level.kenny.velocity.x = level.kenny.terminalVelocity.x;
//                }
            }

            // Bunny Jump
            if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                level.kenny.setJumping(true);
            } else {
                level.kenny.setJumping(false);
            }
        }
    }


    private void onCollisionKennyWithWall(Wall wall) {
        Kenny kenny = level.kenny;
        float heightDifference = Math.abs(kenny.position.y - (  wall.position.y + wall.bounds.height));
        if (heightDifference > 0.25f) {
            boolean hitRightEdge = kenny.position.x > ( wall.position.x + wall.bounds.width / 2.0f);
            if (hitRightEdge) {
                kenny.position.x = wall.position.x + wall.bounds.width;
            } else {
                kenny.position.x = wall.position.x - kenny.bounds.width;
            }
            return;
        }

        switch (kenny.jumpState) {
            case GROUNDED:
                break;
            case FALLING:
            case JUMP_FALLING:
                kenny.position.y = wall.position.y + kenny.bounds.height  + kenny.origin.y;
                kenny.jumpState = Kenny.JUMP_STATE.GROUNDED;
                break;
            case JUMP_RISING:
                kenny.position.y = wall.position.y + kenny.bounds.height + kenny.origin.y;
                break;
        }
    }

    private void onCollisionKennyWithBottle(Bottle bottle) {
        bottle.collected = true;
        AudioManager.instance.play(Assets.instance.sounds.pickupBottle);
        score += bottle.getScore();
        Gdx.app.log(TAG, "Musical Note collected");
    }

    private void onCollisionKennyWithPaper(Paper paper) {
        paper.collected = true;
        AudioManager.instance.play(Assets.instance.sounds.pickupPaper);
        score += paper.getScore();
        level.kenny.setPaperPowerup(true);
        Gdx.app.log(TAG, "Paper collected");
    }

    private void onCollisionKennyWithGoal(){
        goalReached = true;
        timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_FINISHED;
        Vector2 centerPosBunnyHead = new Vector2(level.kenny.position);
        centerPosBunnyHead.x += level.kenny.bounds.width;
        spawnCarrots(centerPosBunnyHead, Constants.CARROTS_SPAWN_MAX, Constants.CARROTS_SPAWN_RADIUS);
    }

    private void testCollisions(){
        r1.set(level.kenny.position.x, level.kenny.position.y,
                level.kenny.bounds.width, level.kenny.bounds.height);

        // Test collision: Kenny <-> Walls
        for (Wall wall : level.walls) {
            r2.set(wall.position.x, wall.position.y, wall.bounds.width, wall.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionKennyWithWall(wall);
            // IMPORTANT: must do all collisions for valid
            // edge testing on walls.
        }

        // Test collision: Bunny Head <-> Gold Coins
        for (Bottle bottle : level.bottles) {
            if (bottle.collected) continue;
            r2.set(bottle.position.x, bottle.position.y, bottle.bounds.width, bottle.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionKennyWithBottle(bottle);
            break;
        }

        // Test collision: Bunny Head <-> Papers
        for (Paper paper : level.papers) {
            if (paper.collected) continue;
            r2.set(paper.position.x, paper.position.y, paper.bounds.width, paper.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionKennyWithPaper(paper);
            break;
        }

        // Test collision: Bunny Head <-> Goal
        if (!goalReached) {
            r2.set(level.goal.bounds);
            r2.x += level.goal.position.x;
            r2.y += level.goal.position.y;
            if (r1.overlaps(r2)) onCollisionKennyWithGoal();
        }

    }




    public boolean isGameOver () {
        return lives < 0;
    }

    public boolean isPlayerInWater () {
        return level.kenny.position.y < -5;
    }

    private void backToMenu () {
        // switch to menu screen
        ScreenTransition transition = ScreenTransitionSlide.init(0.75f,
                ScreenTransitionSlide.DOWN, false, Interpolation.bounceOut);
        game.setScreen(new MenuScreen(game), transition);
    }


    // box2d stuff
    private void initPhysics () {
        if (b2world != null) b2world.dispose();
        b2world = new World(new Vector2(0, -9.81f), true);
        // Rocks
        Vector2 origin = new Vector2();
        for (Wall rock : level.walls) {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.KinematicBody;
            bodyDef.position.set(rock.position);
            Body body = b2world.createBody(bodyDef);
            rock.body = body;
            PolygonShape polygonShape = new PolygonShape();
            origin.x = rock.bounds.width / 2.0f;
            origin.y = rock.bounds.height / 2.0f;
            polygonShape.setAsBox(rock.bounds.width / 2.0f, rock.bounds.height / 2.0f, origin, 0);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = polygonShape;
            body.createFixture(fixtureDef);
            polygonShape.dispose();
        }
    }

    private void spawnCarrots (Vector2 pos, int numCarrots, float radius) {
        float carrotShapeScale = 0.5f;
        // create carrots with box2d body and fixture
        for (int i = 0; i<numCarrots; i++) {
            Carrot carrot = new Carrot();
            // calculate random spawn position, rotation, and scale
            float x = MathUtils.random(-radius, radius);
            float y = MathUtils.random(5.0f, 15.0f);
            float rotation = MathUtils.random(0.0f, 360.0f)
                    * MathUtils.degreesToRadians;
            float carrotScale = MathUtils.random(0.5f, 1.5f);
            carrot.scale.set(carrotScale, carrotScale);
            // create box2d body for carrot with start position// and angle of rotation
            BodyDef bodyDef = new BodyDef();
            bodyDef.position.set(pos);
            bodyDef.position.add(x, y);
            bodyDef.angle = rotation;
            Body body = b2world.createBody(bodyDef);
            body.setType(BodyDef.BodyType.DynamicBody);
            carrot.body = body;
            // create rectangular shape for carrot to allow// interactions (collisions) with other objects
            PolygonShape polygonShape = new PolygonShape();
            float halfWidth = carrot.bounds.width / 2.0f * carrotScale;
            float halfHeight = carrot.bounds.height /2.0f * carrotScale;
            polygonShape.setAsBox(halfWidth * carrotShapeScale, halfHeight * carrotShapeScale);
            // set physics attributes
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = polygonShape;
            fixtureDef.density = 50;
            fixtureDef.restitution = 0.5f;
            fixtureDef.friction = 0.5f;
            body.createFixture(fixtureDef);
            polygonShape.dispose();
            // finally, add new carrot to list for updating/rendering
            level.carrots.add(carrot);
        }
    }

    @Override
    public void dispose() {
        if (b2world != null) b2world.dispose();
    }
}
