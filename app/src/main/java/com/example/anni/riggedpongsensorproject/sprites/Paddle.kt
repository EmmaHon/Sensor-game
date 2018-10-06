package com.example.anni.riggedpongsensorproject.sprites

import android.graphics.Paint
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.DENSITY
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.PPM
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.SCALE
import com.example.anni.riggedpongsensorproject.screens.GameScreen

class Paddle(gameScreen: GameScreen, xPos: Float, yPos: Float, side: Int) {

    private val world = gameScreen.getWorld()
    private val screenSide = side
    private lateinit var b2bodyPaddle: Body
    // textures
    private val paddleSprite = Sprite(gameScreen.getAtlasObjects().findRegion("RP_Asset_Bat_LEFT"))
    private val paddleStartX = xPos
    private val paddleStartY = yPos

    init {
        setupPaddle()
    }

    fun getPaddleSprite(): Sprite {
        return paddleSprite
    }

    fun getPaddleBody(): Body {
        return b2bodyPaddle
    }

    private fun setupPaddle() {
        // if time, find a better solution to get the sprite to it's correct position
        paddleSprite.setPosition(paddleStartX - paddleSprite.width/2f,
                                 paddleStartY - paddleSprite.height/2f)
        if (screenSide == 1) {
            paddleSprite.setFlip(true, true)
        }
        createPaddleBody(world, paddleSprite.x + paddleSprite.width/2f,
                        paddleSprite.y + paddleSprite.height/2f, 0x1, 0x2, 0)
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
        val fDef = FixtureDef()
        fDef.shape = paddleShape
        fDef.density = DENSITY
        // collision properties
        fDef.filter.categoryBits = cBits // is a property
        fDef.filter.maskBits = mBits // collides with a property
        fDef.filter.groupIndex = gIndex
        b2bodyPaddle.createFixture(fDef)
        paddleShape.dispose()
    }

    private fun movePaddle() {}
}