package com.example.anni.riggedpongsensorproject.sprites

import android.util.Log
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.example.anni.riggedpongsensorproject.screens.GameScreen
import com.example.anni.riggedpongsensorproject.utils.VectorUtils
import com.badlogic.gdx.physics.box2d.BodyDef
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.DENSITY
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.PPM
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.SCALE
import com.example.anni.riggedpongsensorproject.managers.AssetManager
import com.example.anni.riggedpongsensorproject.utils.ObjectBits
import com.example.anni.riggedpongsensorproject.utils.PaddleHit
import kotlin.experimental.or

class GameObjectBall(gameScreen: GameScreen) {

    private val game = gameScreen
    private val camera = gameScreen.getCamera()
    private val world = gameScreen.getWorld()
    private lateinit var b2bodyBall: Body
    // sprite
    private val ballSprite = Sprite(AssetManager.ball)
    // features
    private val ballRadius = 32f
    private val ballRestitution = 0.5f
    // Movement
    private val MAX_SPEED = 350f
    private val MAX_ACCELERATION = 20f
    private val MAX_DECELERATION = MAX_ACCELERATION / 2
    private val acceleration = Vector2()
    private var numberOfTicks = 0
    var position = Vector2() //ball position
    var velocity = Vector2()
    var firstStarted = true
    var paddleContact = false
    var previousPaddle = PaddleHit.NO_PADDLE
    var currentPaddle = PaddleHit.NO_PADDLE

    init {
        setupBallObject()
    }


    fun getBallBody(): Body {
        return b2bodyBall
    }

    fun getBallSprite(): Sprite {
        return ballSprite
    }

    fun setCenterDimensions() {
        Log.d("DEBUGMOVE", "bound ball to center")
        if (VectorUtils.adjustByRangeX(position, Gdx.graphics.width / 2f, Gdx.graphics.width / 2f))
            velocity.x = 0f
        if (VectorUtils.adjustByRangeY(position, Gdx.graphics.height / 2f, Gdx.graphics.height / 2f))
            velocity.y = 0f
    }

    private fun setHitPaddleDimension(paddleLeft: Paddle, paddleRight: Paddle) {
        val height = camera.viewportHeight * SCALE
        val width = camera.viewportWidth * SCALE
        Log.d("DEBUG1", "paddle LEFT POS: ${paddleLeft.getPaddleBodyPosition()} " +
                "and paddle RIGHT POS: ${paddleRight.getPaddleBodyPosition()}")
        Log.d("DEBUG2", "position: $position")
        if (VectorUtils.adjustByRangeX(position, 250f, width - 250f)) {
            velocity.x = 0f
            Log.d("DEBUG3","ADJUSTRANGE X: min x: 220f and max x: ${width-220f}")
        }
        /*if (VectorUtils.adjustByRangeY(position, height/ 2f - paddleLeft.getPaddleSprite().height/2f,
                        height/ 2f + paddleLeft.getPaddleSprite().height/2f)) {
            velocity.y = 0f
            Log.d("DEBUG4", "ADJUSTRANGE Y: min y: ${height/ 2f - paddleLeft.getPaddleSprite().height/2f}" +
                    " and max y: ${height/ 2f + paddleLeft.getPaddleSprite().height/2f}")
        }*/
    }

    fun moveBall(delta: Float, paddleLeft: Paddle, paddleRight: Paddle) {
        numberOfTicks++
        // check if the sensor is available
        if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
            // set the acceleration base on the accelerometer input
            // inverted axis because the game is displayed in landscape mode
            if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
                acceleration.set(Gdx.input.accelerometerY, Gdx.input.accelerometerX)
            }
            when {
                firstStarted -> {
                    setCenterDimensions()
                    firstStarted = false
                }
                paddleContact -> {
                    setHitPaddleDimension(paddleLeft, paddleRight)
                }
            }
            // set the acceleration bounds
            VectorUtils.adjustByRange(acceleration, -2f, 2f)
            // set the input deadzone
            if (!VectorUtils.adjustDeadzone(acceleration, 1f, 0f)) {
                // we're out of the deadzone, so let's adjust the acceleration
                // (2 is 100% of the max acceleration)
                acceleration.x = (acceleration.x / 2 * MAX_ACCELERATION)
                acceleration.y = (-acceleration.y / 2 * MAX_ACCELERATION)
            }

            // if there is no acceleration and the ball is moving, calculate
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
            // modify and check the ball's velocity
            velocity.add(acceleration)
            VectorUtils.adjustByRange(velocity, -MAX_SPEED, MAX_SPEED)
            // modify and check the ball's position, applying the delta parameter
            position.add(velocity.x * delta, velocity.y * delta)
            // check the ball's position against the stage's dimensions, correcting it if
            // needed and zeroing the velocity, so that the ball stops moving in the
            // current direction.
            if (VectorUtils.adjustByRangeX(position, ballSprite.width / 2f, (Gdx.graphics.width - ballSprite.width / 2f)))
                velocity.x = 0f
            if (VectorUtils.adjustByRangeY(position, 100f, (Gdx.graphics.height - 100f)))
                velocity.y = 0f

            // player controls y- and x-axis
            ballSprite.setPosition(
                    ((b2bodyBall.position.x * PPM * SCALE) - ballSprite.width / 2f),
                    ((b2bodyBall.position.y * PPM * SCALE) - ballSprite.height / 2f))
            b2bodyBall.setTransform((position.x) / PPM / SCALE, position.y / PPM / SCALE, b2bodyBall.angle)
        }
    }

    private fun setupBallObject() {
        // position sprite the center of the screen
        ballSprite.setPosition(camera.viewportWidth / 2f - ballSprite.width / 2f,
                camera.viewportHeight / 2f - ballSprite.height / 2f)
        createBallBody(world, ballSprite.x + ballSprite.width / 2f, ballSprite.y + ballSprite.height / 2f,
                ballRadius, ObjectBits.BALL.bits, ObjectBits.PADDLE.bits or
                ObjectBits.WALL.bits, 0)
    }

    // create physics body
    private fun createBallBody(world: World, xPos: Float, yPos: Float, radius: Float,
                               cBits: Short, mBits: Short, gIndex: Short) {
        var bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
        bodyDef.fixedRotation = true
        bodyDef.position.set(xPos / PPM / SCALE, yPos / PPM / SCALE)
        b2bodyBall = world.createBody(bodyDef)
        val roundShape = CircleShape()
        roundShape.radius = (radius / PPM / SCALE)
        var fDef = FixtureDef()
        fDef.shape = roundShape
        fDef.density = DENSITY
        //affects "bounciness"
        fDef.restitution = ballRestitution
        // collision
        fDef.filter.categoryBits = cBits // is a property
        fDef.filter.maskBits = mBits // collides with a property
        fDef.filter.groupIndex = gIndex
        b2bodyBall.createFixture(fDef)
        roundShape.dispose()
        // return b2body
    }

}