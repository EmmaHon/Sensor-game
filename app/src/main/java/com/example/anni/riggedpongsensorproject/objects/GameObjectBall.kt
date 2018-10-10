package com.example.anni.riggedpongsensorproject.objects

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
import com.example.anni.riggedpongsensorproject.utils.Paddles

class GameObjectBall(game: GameScreen) {

    companion object {
        private const val MAX_SPEED = 350f
        private const val MAX_ACCELERATION = 20f
        private const val MAX_DECELERATION = MAX_ACCELERATION / 2
    }

    private val camera = game.getCamera()
    private val world = game.getWorld()
    private val ballSprite = Sprite(AssetManager.ball)
    private val ballRadius = 32f
    private val ballRestitution = 0.5f
    private val acceleration = Vector2()
    private var numberOfTicks = 0
    private var position = Vector2()
    private var velocity = Vector2()
    private var firstStarted = true
    private lateinit var b2bodyBall: Body
    var paddleContact = false
    var previousPaddle = Paddles.NO_PADDLE
    var currentPaddle = Paddles.NO_PADDLE

    init {
        setupBallObject()
    }

    fun getBallBody(): Body {
        return b2bodyBall
    }

    fun getBallSprite(): Sprite {
        return ballSprite
    }

    // center ball in the middle of the screen
    fun setCenterDimensions() {
        if (VectorUtils.adjustByRangeX(position, Gdx.graphics.width / 2f, Gdx.graphics.width / 2f))
            velocity.x = 0f
        if (VectorUtils.adjustByRangeY(position, Gdx.graphics.height / 2f, Gdx.graphics.height / 2f))
            velocity.y = 0f
    }

    // prevent from going through paddles
    private fun setHitPaddleDimension() {
        val width = camera.viewportWidth * SCALE
        if (VectorUtils.adjustByRangeX(position, 250f, width - 250f)) {
            velocity.x = 0f
        }
    }

    fun moveBall(delta: Float) {
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
                    setHitPaddleDimension()
                }
            }
            // set the acceleration bounds
            VectorUtils.adjustByRange(acceleration, -2f, 2f)
            // set the input deadzone
            if (!VectorUtils.adjustDeadzone(acceleration, 1f, 0f)) {
                // we're out of the deadzone, so let's adjust the acceleration
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
            // modify and check the player's velocity
            velocity.add(acceleration)
            VectorUtils.adjustByRange(velocity, -MAX_SPEED, MAX_SPEED)
            // modify and check the player's position
            position.add(velocity.x * delta, velocity.y * delta)
            // check the player's position against the stage's dimensions, correcting it if
            // needed and zeroing the velocity, so that the ball stops moving in the
            // current direction.
            if (VectorUtils.adjustByRangeX(position, ballSprite.width / 2f, (Gdx.graphics.width - ballSprite.width / 2f)))
                velocity.x = 0f
            if (VectorUtils.adjustByRangeY(position, 100f, (Gdx.graphics.height - 100f)))
                velocity.y = 0f

            // set the actual position of the player
            ballSprite.setPosition(
                    ((b2bodyBall.position.x * PPM * SCALE) - ballSprite.width / 2f),
                    ((b2bodyBall.position.y * PPM * SCALE) - ballSprite.height / 2f))
            b2bodyBall.setTransform((position.x) / PPM / SCALE, position.y / PPM / SCALE, b2bodyBall.angle)
        }
    }

    private fun setupBallObject() {
        // position sprite to the center of the screen
        ballSprite.setPosition(camera.viewportWidth / 2f - ballSprite.width / 2f,
                camera.viewportHeight / 2f - ballSprite.height / 2f)
        createBallBody(world, ballSprite.x + ballSprite.width / 2f, ballSprite.y + ballSprite.height / 2f,
                ballRadius, ObjectBits.BALL.bits, ObjectBits.PADDLE.bits, 0)
    }

    private fun createBallBody(world: World, xPos: Float, yPos: Float, radius: Float,
                               cBits: Short, mBits: Short, gIndex: Short) {
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
        bodyDef.fixedRotation = true
        bodyDef.position.set(xPos / PPM / SCALE, yPos / PPM / SCALE)
        b2bodyBall = world.createBody(bodyDef)
        val roundShape = CircleShape()
        roundShape.radius = (radius / PPM / SCALE)
        val fDef = FixtureDef()
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
    }
}