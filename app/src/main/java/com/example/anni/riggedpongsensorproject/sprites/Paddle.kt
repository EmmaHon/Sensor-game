package com.example.anni.riggedpongsensorproject.sprites

import android.support.annotation.FloatRange
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.DENSITY
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.PPM
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.SCALE
import com.example.anni.riggedpongsensorproject.managers.AssetManager
import com.example.anni.riggedpongsensorproject.screens.GameScreen
import com.example.anni.riggedpongsensorproject.utils.ObjectBits
import com.example.anni.riggedpongsensorproject.utils.random
import kotlin.math.sin

class Paddle(gameScreen: GameScreen, xPos: Float, yPos: Float, side: Int) {

    private val world = gameScreen.getWorld()
    private val camera = gameScreen.getCamera()
    private val screenSide = side
    private lateinit var b2bodyPaddle: Body
    private val paddleSprite = Sprite(AssetManager.paddle)
    private val paddleStartX = xPos
    private val paddleStartY = yPos
    private var numberOfTicks = 0
    private val MAX_SPEED = 20
    private var speedY = 100

    init {
        setupPaddle()
    }

    fun getPaddleSprite(): Sprite {
        return paddleSprite
    }

    fun getPaddleBody(): Body {
        return b2bodyPaddle
    }

    fun getPaddleBodyPosition(): Vector2 {
        return b2bodyPaddle.position
    }

    fun movePaddle(delta: Float) {
        // up-down paddle movement with sinusoidal motion
        // TODO: random speeds (at a specific range) and direction
        val maxTop = camera.viewportHeight - 110f
        val maxDown = 300f
        //if (speedY >= MAX_SPEED)
        numberOfTicks++
        paddleSprite.y = (maxDown * sin(numberOfTicks * 0.5f * Math.PI/ speedY).toFloat()) + maxTop
        b2bodyPaddle.setTransform(b2bodyPaddle.position.x,(paddleSprite.y + paddleSprite.height/2f)/ PPM/ SCALE, b2bodyPaddle.angle)
    }

    private fun setupPaddle() {
        paddleSprite.setPosition(paddleStartX - paddleSprite.width/2f,
                                 paddleStartY - paddleSprite.height/2f)
        if (screenSide == 1) {
            paddleSprite.setFlip(true, true)
        }
        createPaddleBody(world, paddleSprite.x + paddleSprite.width/2f,
                        paddleSprite.y + paddleSprite.height/2f, ObjectBits.PADDLE.bits,
                        ObjectBits.BALL.bits, 0)
        val speedValues = IntRange(20, 100)
        speedY = speedValues.random()
    }

    private fun createPaddleBody(world: World, xPos: Float, yPos: Float,
                                 cBits: Short, mBits: Short, gIndex: Short) {
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.KinematicBody
        bodyDef.fixedRotation = false
        // Set our body to the same position as our sprite (for ball and bats)
        bodyDef.position.set(xPos/ PPM/ SCALE, yPos/ PPM/ SCALE)
        // Create a body in the world using our definition
        b2bodyPaddle = world.createBody(bodyDef)
        // Now define the dimensions of the physics shape
        val paddleShape = PolygonShape()
        paddleShape.setAsBox(paddleSprite.width/ PPM/ 4f, paddleSprite.height/ PPM/ 4f)
        //paddleShape.setAsBox(paddleSprite.width/ 2f, paddleSprite.height/ 2f)
        var fDef = FixtureDef()
        fDef.shape = paddleShape
        fDef.density = DENSITY
        // collision properties
        fDef.filter.categoryBits = cBits // is a property
        fDef.filter.maskBits = mBits // collides with a property
        fDef.filter.groupIndex = gIndex
        b2bodyPaddle.createFixture(fDef)
        paddleShape.dispose()
    }
}