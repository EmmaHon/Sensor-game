package com.example.anni.riggedpongsensorproject.sprites

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.example.anni.riggedpongsensorproject.screens.GameScreen
import com.example.anni.riggedpongsensorproject.utils.VectorUtils
import com.badlogic.gdx.physics.box2d.BodyDef
import com.example.anni.riggedpongsensorproject.RiggedPong
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.DENSITY
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.PPM
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.SCALE

class GameObjectBall(mGameScreen: GameScreen, xPos: Float, yPos: Float) : Sprite() {

    private val world = mGameScreen.getWorld()
    private lateinit var b2bodyBall: Body
    // Sprite Texture
    private val ballTextureRegion = mGameScreen.getAtlasObjects().findRegion("RP_Asset_Ball")
    private val ballTextureHeight = ballTextureRegion.originalHeight.toFloat()
    private val ballTextureWidth = ballTextureRegion.originalWidth.toFloat()
    private val ballRadius = 32f
    private val ballRestitution = 1f
    // Movement
    private val MAX_SPEED = 240f
    private val MAX_ACCELERATION = 10f
    private val MAX_DECELERATION = MAX_ACCELERATION / 2
    private val ballStartX = xPos
    private val ballStartY = yPos
    private val position = Vector2() //ball position
    private val velocity = Vector2()
    private val acceleration = Vector2()

    //(Gdx.graphics.width/ 2f - ballTextureWidth/ 2f)
    init {
        createBallBody(world, ballStartX, ballStartY, ballRadius)
        // position to the center of the screen and size of the ball
        setBounds(getBallBody().position.x * 61.5f , getBallBody().position.y * 60f , ballTextureWidth, ballTextureHeight)
        setRegion(ballTextureRegion)
    }

    fun getBallBody(): Body {
        return b2bodyBall
    }

    private fun createBallBody(world: World, xPos: Float, yPos: Float, radius: Float) {
        var bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
        bodyDef.fixedRotation = true
        bodyDef.position.set(xPos/ PPM/ SCALE, yPos/ PPM/ SCALE)
        b2bodyBall = world.createBody(bodyDef)
        val roundShape = CircleShape()
        roundShape.radius = (radius/ PPM/ SCALE)
        b2bodyBall.createFixture(roundShape, DENSITY)
        //fixtureDefinition.restitution affects "bounciness"
        roundShape.dispose()
        // return b2body
    }

    fun moveBall(delta: Float) {
        //Log.d("RiggedPong.LOG", "${Gdx.input.accelerometerX}, ${Gdx.input.accelerometerY}, ${Gdx.input.accelerometerZ}")
        // check the input and calculate the acceleration
        if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {

            // set the acceleration base on the accelerometer input; notice the
            // inverted axis because the game is displayed in landscape mode
            acceleration.set(Gdx.input.accelerometerY, Gdx.input.accelerometerX)

            // set the acceleration bounds
            VectorUtils.adjustByRange(acceleration, -2f, 2f)

            // set the input deadzone
            if (!VectorUtils.adjustDeadzone(acceleration, 1f, 0f)) {
                // we're out of the deadzone, so let's adjust the acceleration
                // (2 is 100% of the max acceleration)
                acceleration.x = (acceleration.x / 2 * MAX_ACCELERATION)
                acceleration.y = (-acceleration.y / 2 * MAX_ACCELERATION)
            }
        } else {
            // when the keys aren't pressed the acceleration will be zero, so
            // the ship's velocity won't be affected by it
            acceleration.x = when {
                Gdx.input.isKeyPressed(Input.Keys.LEFT) -> -MAX_ACCELERATION
                Gdx.input.isKeyPressed(Input.Keys.RIGHT) -> MAX_ACCELERATION
                else -> 0f
            }
            acceleration.y = when {
                Gdx.input.isKeyPressed(Input.Keys.UP) -> MAX_ACCELERATION
                Gdx.input.isKeyPressed(Input.Keys.DOWN) -> -MAX_ACCELERATION
                else -> 0f
            }
        }
        // if there is no acceleration and the ship is moving, let's calculate
        // an appropriate deceleration
        if (acceleration.len() == 0f && velocity.len() > 0f) {
            // horizontal deceleration
            if (velocity.x > 0) {
                acceleration.x = -MAX_DECELERATION
                if (velocity.x - acceleration.x < 0) {
                    acceleration.x = -(acceleration.x - velocity.x)
                }
            } else if (velocity.x < 0) {
                acceleration.x = MAX_DECELERATION
                if (velocity.x + acceleration.x > 0) {
                    acceleration.x = (acceleration.x - velocity.x)
                }
            }
            // vertical deceleration
            if (velocity.y > 0) {
                acceleration.y = -MAX_DECELERATION
                if (velocity.y - acceleration.y < 0) {
                    acceleration.y = -(acceleration.y - velocity.y)
                }
            } else if (velocity.y < 0) {
                acceleration.y = MAX_DECELERATION
                if (velocity.y + acceleration.y > 0) {
                    acceleration.y = (acceleration.y - velocity.y)
                }
            }
        }
        // modify and check the ship's velocity
        velocity.add(acceleration)
        VectorUtils.adjustByRange(velocity, -MAX_SPEED, MAX_SPEED)

        // modify and check the ship's position, applying the delta parameter
        position.add(velocity.x * delta, velocity.y * delta)

        // we can't let the ship go off the screen, so here we check the new
        // ship's position against the stage's dimensions, correcting it if
        // needed and zeroing the velocity, so that the ship stops flying in the
        // current direction
        if (VectorUtils.adjustByRangeX(position, 0f, (Gdx.graphics.width - width)))
            velocity.x = 0f
        if (VectorUtils.adjustByRangeY(position, 0f, (Gdx.graphics.height - height)))
            velocity.y = 0f

        // update the ship's actual position
        x = position.x
        y = position.y
    }
}