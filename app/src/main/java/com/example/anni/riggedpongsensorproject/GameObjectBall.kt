package com.example.anni.riggedpongsensorproject

import android.util.Log
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.*

class GameObjectBall(mWorld: World, mGameScreen: GameScreen): Sprite() {

    val world = mWorld
    lateinit var b2body: Body
    private var gameScreen = mGameScreen
    //private val gameScreen = mGameScreen
    //private val ballTexture = Texture("")
    private var ballStand = TextureRegion()

    init {
        val ballRegion = gameScreen.getAtlas().findRegion("RP_Asset_Ball")
        createBall()
        setBounds(0f,0f,60f, 60f)
        setRegion(ballRegion)
        ballStand.setRegion(ballRegion)
    }

    private fun createBall() {
        Log.d("DEBUG", "create ball sprite")
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