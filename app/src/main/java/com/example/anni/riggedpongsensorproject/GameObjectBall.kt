package com.example.anni.riggedpongsensorproject

import android.util.Log
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.example.anni.riggedpongsensorproject.Utils.VectorUtils
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.*


class GameObjectBall(mWorld: World, mGameScreen: GameScreen) : Sprite() {

    // finals
    companion object {
        val BALL_STATE_NORMAL = 0
        val BALL_STATE_HIT_WALL = 1
        val BALL_STATE_HIT_BAT = 2
    }

    private val world = mWorld
    private lateinit var b2body: Body
    private var gameScreen = mGameScreen

    // Sprite Texture
    private val ballTextureRegion = gameScreen.getAtlasObjects().findRegion("RP_Asset_Ball")
    private val ballTextureHeight = ballTextureRegion.originalHeight.toFloat()
    private val ballTextureWidth = ballTextureRegion.originalWidth.toFloat()
    //private var ballStand = TextureRegion()

    // ball movement
    private val MAX_SPEED = 240f
    private val MAX_ACCELERATION = 10f
    private val MAX_DECELERATION = MAX_ACCELERATION / 2
    private val position = Vector2() //ball position
    private val velocity = Vector2()
    private val acceleration = Vector2()

    init {
        createBall()
        // To center it, you need to subtract half of the textures width from the x,
        // and half of the textures height from the y coordinate. Something along these lines:
        val xBallPosition: Float = (Gdx.graphics.width/ 2f - ballTextureWidth/ 2f)
        val yBallPosition: Float = (Gdx.graphics.height/ 2f - ballTextureHeight/ 2f)
        // position to the center of the screen and size of the ball
        setBounds(xBallPosition, yBallPosition, ballTextureWidth, ballTextureHeight)
        setRegion(ballTextureRegion)
        //ballStand.setRegion(ballTextureRegion)
    }

    private fun createBall() {
        var bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
        bodyDef.position.set(32f, 32f)

        b2body = world.createBody(bodyDef)

        val fDef = FixtureDef()
        val roundShape = CircleShape()
        roundShape.radius = (5f)
        fDef.shape = roundShape
        b2body.createFixture(fDef)

    }

    fun setInitialPosition(x: Float, y: Float) {
        position.set(x, y)
    }

    fun moveBall(delta: Float) {
        //Log.d("RiggedPong.LOG","${Gdx.input.accelerometerX}, ${Gdx.input.accelerometerY}, ${Gdx.input.accelerometerZ}")

        // check the input and calculate the acceleration
        if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {

            // set the acceleration base on the accelerometer input; notice the
            // inverted axis because the game is displayed in landscape mode
            acceleration.set(Gdx.input.getAccelerometerY(), Gdx.input.getAccelerometerX());

            // set the acceleration bounds
            VectorUtils.adjustByRange(acceleration, -2f, 2f);

            // set the input deadzone
            if (!VectorUtils.adjustDeadzone(acceleration, 1f, 0f)) {
                // we're out of the deadzone, so let's adjust the acceleration
                // (2 is 100% of the max acceleration)
                acceleration.x = (acceleration.x / 2 * MAX_ACCELERATION);
                acceleration.y = (-acceleration.y / 2 * MAX_ACCELERATION);
            }
        } else {
            // when the keys aren't pressed the acceleration will be zero, so
            // the ship's velocity won't be affected by it
            acceleration.x = if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                -MAX_ACCELERATION
            else
                if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) MAX_ACCELERATION else 0f
            acceleration.y = if (Gdx.input.isKeyPressed(Input.Keys.UP))
                MAX_ACCELERATION
            else
                if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) -MAX_ACCELERATION else 0f
        }
        // if there is no acceleration and the ship is moving, let's calculate
        // an appropriate deceleration
        if (acceleration.len() == 0f && velocity.len() > 0f) {
            // horizontal deceleration
            if (velocity.x > 0) {
                acceleration.x = -MAX_DECELERATION;
                if (velocity.x - acceleration.x < 0) {
                    acceleration.x = -(acceleration.x - velocity.x);
                }
            } else if (velocity.x < 0) {
                acceleration.x = MAX_DECELERATION;
                if (velocity.x + acceleration.x > 0) {
                    acceleration.x = (acceleration.x - velocity.x);
                }
            }
            // vertical deceleration
            if (velocity.y > 0) {
                acceleration.y = -MAX_DECELERATION;
                if (velocity.y - acceleration.y < 0) {
                    acceleration.y = -(acceleration.y - velocity.y);
                }
            } else if (velocity.y < 0) {
                acceleration.y = MAX_DECELERATION;
                if (velocity.y + acceleration.y > 0) {
                    acceleration.y = (acceleration.y - velocity.y);
                }
            }
        }
        // modify and check the ship's velocity
        velocity.add(acceleration);
        VectorUtils.adjustByRange(velocity, -MAX_SPEED, MAX_SPEED);

        // modify and check the ship's position, applying the delta parameter
        position.add(velocity.x * delta, velocity.y * delta);

        // we can't let the ship go off the screen, so here we check the new
        // ship's position against the stage's dimensions, correcting it if
        // needed and zeroing the velocity, so that the ship stops flying in the
        // current direction
        if (VectorUtils.adjustByRangeX(position, 0f, (Gdx.graphics.getWidth() - getWidth())))
            velocity.x = 0f;
        if (VectorUtils.adjustByRangeY(position, 0f, (Gdx.graphics.getHeight() - getHeight())))
            velocity.y = 0f;

        // update the ship's actual position
        setX(position.x);
        setY(position.y);
    }

    override fun draw(batch: Batch) {
        super.draw(batch)
    }
}