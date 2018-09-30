package com.example.anni.riggedpongsensorproject

import android.util.Log
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.*

class GameObjectBall(mWorld: World, mGameScreen: GameScreen): Sprite() {

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
    private val ballTextureRegion = gameScreen.getAtlas().findRegion("RP_Asset_Ball")
    private val ballTextureHeight = ballTextureRegion.originalHeight.toFloat()
    private val ballTextureWidth = ballTextureRegion.originalWidth.toFloat()
    //private var ballStand = TextureRegion()

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

    override fun draw(batch: Batch) {
        super.draw(batch)
    }
}