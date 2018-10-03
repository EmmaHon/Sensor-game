package com.example.anni.riggedpongsensorproject.sprites

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.*
import com.example.anni.riggedpongsensorproject.RiggedPong
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.DENSITY
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.PPM
import com.example.anni.riggedpongsensorproject.screens.GameScreen

class Bat(mGameScreen: GameScreen, side: Int) : Sprite() {

    private val world = mGameScreen.getWorld()
    private val screenSide = side
    private lateinit var b2bodyBat: Body
    // textures
    private val batTextureRegion = mGameScreen.getAtlasObjects().findRegion("RP_Asset_Bat_LEFT")
    private val batHeight = batTextureRegion.originalHeight.toFloat()
    private val batWidth = batTextureRegion.originalWidth.toFloat()

    private val BAT_LEFT_START_X = 200f
    private val BAT_RIGHT_START_X = 1525f
    private val BATS_START_Y = (Gdx.graphics.height / 2f - batHeight / 2f)

    init {
        setRegion(batTextureRegion)
        setupBat()
    }

    private fun setupBat() {
        if (screenSide == 0) {
            setBounds(BAT_LEFT_START_X, BATS_START_Y, batWidth, batHeight)
            setPosition(BAT_LEFT_START_X, BATS_START_Y)
            createBatBody(world, BAT_LEFT_START_X/ 2, BATS_START_Y/ 2)
        } else {
            setBounds(BAT_RIGHT_START_X, BATS_START_Y, batWidth, batHeight)
            setPosition(BAT_RIGHT_START_X, BATS_START_Y)
            setFlip(true, true)
            createBatBody(world, BAT_RIGHT_START_X/ 2, BATS_START_Y/ 2)
        }
        setRegion(batTextureRegion)
    }

    fun getBatBody(): Body {
        return b2bodyBat
    }

    private fun createBatBody(world: World, xPos: Float, yPos: Float) {
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.KinematicBody
        bodyDef.fixedRotation = true
        // Set our body to the same position as our sprite (for ball and bats)
        bodyDef.position.set(xPos/ PPM, yPos/ PPM)
        // Create a body in the world using our definition
        b2bodyBat = world.createBody(bodyDef)
        // Now define the dimensions of the physics shape
        val batShape = PolygonShape()
        batShape.setAsBox((batWidth/ 4/ PPM), (batHeight/ 4/ PPM))
        b2bodyBat.createFixture(batShape, DENSITY)
        batShape.dispose()
        //return b2body
    }
}