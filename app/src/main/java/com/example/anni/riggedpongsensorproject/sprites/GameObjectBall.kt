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
import com.example.anni.riggedpongsensorproject.utils.ObjectBits
import kotlin.experimental.or

class GameObjectBall(gameScreen: GameScreen) {

    private val camera = gameScreen.getCamera()
    private val world = gameScreen.getWorld()
    private lateinit var b2bodyBall: Body
    // sprite
    private val ballSprite = Sprite(gameScreen.getAtlasObjects().findRegion("RP_Asset_Ball"))
    // features
    private val ballRadius = 32f
    private val ballRestitution = 0.5f
    // Movement
    private val MAX_SPEED = 440f
    private val MAX_ACCELERATION = 20f
    private val MAX_DECELERATION = MAX_ACCELERATION / 2
    private val position = Vector2() //ball position
    private val velocity = Vector2()
    private val acceleration = Vector2()
    private var isMoving = true

    init {
        setupBallObject()
    }

    fun getBallBody(): Body {
        return b2bodyBall
    }

    fun getBallSprite(): Sprite {
        return ballSprite
    }

    fun moveBall(delta: Float) {
        if (isMoving) {
            // check the input and calculate the acceleration
            if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
                // set the acceleration base on the accelerometer input; notice the
                // inverted axis because the game is displayed in landscape mode
                var renderX =  Gdx.input.accelerometerX
                var renderY = Gdx.input.accelerometerY
                acceleration.set(renderY, renderX)
                // set the acceleration bounds
                VectorUtils.adjustByRange(acceleration, -20f, 20f)

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
                // current direction
                if (VectorUtils.adjustByRangeX(position, ballSprite.width / 2f, (Gdx.graphics.width - ballSprite.width / 2f)))
                    velocity.x = 0f
                if (VectorUtils.adjustByRangeY(position, 100f, (Gdx.graphics.height - 100f)))
                    velocity.y = 0f

                // update the ball's actual position
                ballSprite.setPosition(
                        (b2bodyBall.position.x * PPM * SCALE) - ballSprite.width / 2f,
                        (b2bodyBall.position.y * PPM * SCALE) - ballSprite.height / 2f)
                // update position of the box2dBody
                b2bodyBall.setTransform((position.x) / PPM / SCALE, position.y / PPM / SCALE, b2bodyBall.angle)
            }
        }
    }

    fun setIsMoving(moving: Boolean) {
        isMoving = moving
    }

    private fun setupBallObject() {
        // position sprite the center of the screen
        ballSprite.setPosition(camera.viewportWidth/2f - ballSprite.width/2f,
                               camera.viewportHeight/2f - ballSprite.height/2f)
        createBallBody(world, ballSprite.x + ballSprite.width/2f, ballSprite.y + ballSprite.height/2f,
                       ballRadius, ObjectBits.BALL.bits,ObjectBits.PADDLE.bits or
                       ObjectBits.WALL.bits, 0)
    }

    // create physics body
    private fun createBallBody(world: World, xPos: Float, yPos: Float, radius: Float,
                               cBits: Short, mBits: Short, gIndex: Short) {
        var bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
        bodyDef.fixedRotation = true
        bodyDef.position.set(xPos/ PPM/ SCALE, yPos/PPM/ SCALE)
        b2bodyBall = world.createBody(bodyDef)
        val roundShape = CircleShape()
        roundShape.radius = (radius/ PPM/ SCALE)
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