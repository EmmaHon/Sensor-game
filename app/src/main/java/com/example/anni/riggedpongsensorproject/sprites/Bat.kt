package com.example.anni.riggedpongsensorproject.sprites

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.*
import com.example.anni.riggedpongsensorproject.screens.GameScreen

class Bat(mGameScreen: GameScreen, side: Int) : Sprite() {

    private val world = mGameScreen.getWorld()
    private val screenSide = side
    private lateinit var b2bodyBat: Body
    // textures
    private val batTextureRegion = mGameScreen.getAtlasObjects().findRegion("RP_Asset_Bat_LEFT")
    private val batTextureHeight = batTextureRegion.originalHeight.toFloat()
    private val batTextureWidth = batTextureRegion.originalWidth.toFloat()

    private val BAT_LEFT_START_X = 200f
    private val BAT_RIGHT_START_X = 1525f
    private val BATS_START_Y = (Gdx.graphics.height / 2f - batTextureHeight / 2f)

    init {
        setRegion(batTextureRegion)
        setupBat()
    }

    private fun setupBat() {
        if (screenSide == 0) {
            setBounds(BAT_LEFT_START_X, BATS_START_Y, batTextureWidth, batTextureHeight)
            setPosition(BAT_LEFT_START_X, BATS_START_Y)
        } else {
            setBounds(BAT_RIGHT_START_X, BATS_START_Y, batTextureWidth, batTextureHeight)
            setFlip(true, true)
            setPosition(BAT_RIGHT_START_X, BATS_START_Y)
        }
        createBatBody(world, batTextureWidth, batTextureHeight)
    }

    private fun createBatBody(world: World, width: Float, height: Float) {
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.KinematicBody
        bodyDef.fixedRotation = true
        // 1 to 1 dimensions, meaning 1 in physics engine is 1 pixel
        // Set our body to the same position as our sprite (for ball and bats)
        bodyDef.position.set(this.x, this.y)
        // Now define the dimensions of the physics shape
        val batShape = PolygonShape()
        batShape.setAsBox(width , width)
        var fixtureDef = FixtureDef()
        fixtureDef.shape = batShape
        fixtureDef.density = 1f
        // Create a body in the world using our definition
        b2bodyBat = world.createBody(bodyDef)
        b2bodyBat.createFixture(fixtureDef).userData = this
        // Shape is the only disposable of the lot, so get rid of it
        //batShape.dispose()
        //return b2body
    }
}