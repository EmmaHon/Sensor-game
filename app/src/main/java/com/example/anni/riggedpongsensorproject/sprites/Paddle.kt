package com.example.anni.riggedpongsensorproject.sprites

import android.graphics.Paint
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.DENSITY
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.PPM
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.SCALE
import com.example.anni.riggedpongsensorproject.screens.GameScreen

class Paddle(gameScreen: GameScreen, xPos: Float, yPos: Float, side: Int) : Sprite() {

    private val world = gameScreen.getWorld()
    private val screenSide = side
    private lateinit var b2bodyPaddle: Body
    // textures
    private val paddleTextureRegion = gameScreen.getAtlasObjects().findRegion("RP_Asset_Bat_LEFT")
    private val paddleHeight = paddleTextureRegion.originalHeight.toFloat()
    private val paddleWidth = paddleTextureRegion.originalWidth.toFloat()
    private val paddleStartX = xPos
    private val paddleStartY = yPos

    init {
        setRegion(paddleTextureRegion)
        setupPaddle()
    }

    fun getPaddleBody(): Body {
        return b2bodyPaddle
    }

    private fun setupPaddle() {
        createPaddleBody(world, paddleStartX, paddleStartY, false)
        if (screenSide == 1) {
            setBounds(getPaddleBody().position.x * 63f, getPaddleBody().position.y * 51f, paddleWidth, paddleHeight)
            setFlip(true, true)
        } else {
            setBounds(getPaddleBody().position.x * 55f, getPaddleBody().position.y * 51f, paddleWidth, paddleHeight)
        }
        setRegion(paddleTextureRegion)
    }

    private fun createPaddleBody(world: World, xPos: Float, yPos: Float, isSensor: Boolean) {
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.KinematicBody
        bodyDef.fixedRotation = true
        // Set our body to the same position as our sprite (for ball and bats)
        bodyDef.position.set(xPos/ PPM/ SCALE, yPos/ PPM/ SCALE)
        // Create a body in the world using our definition
        b2bodyPaddle = world.createBody(bodyDef)
        // Now define the dimensions of the physics shape
        val paddleShape = PolygonShape()
        paddleShape.setAsBox(paddleWidth/ PPM/ 4f, paddleHeight/ PPM/ 4f)
        val fDef = FixtureDef()
        fDef.shape = paddleShape
        fDef.density = DENSITY
        fDef.isSensor = isSensor
        b2bodyPaddle.createFixture(fDef)
        paddleShape.dispose()
        //return b2body
    }

}