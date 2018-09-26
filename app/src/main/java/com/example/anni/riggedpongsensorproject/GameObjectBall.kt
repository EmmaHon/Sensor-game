package com.example.anni.riggedpongsensorproject

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.*

class GameObjectBall(mWorld: World, mGameScreen: Screen): Sprite() {

    private val world = mWorld
    private val gameScreen = mGameScreen
    private lateinit var body: Body
    private val ballTexture = Texture("")

    init {
        gameScreen.getAtlas().findRegion("RP_Asset_Ball")
        createBall()
    }

    private fun createBall() {
        var bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
        bodyDef.position.set(0f, 0f)

        body = world.createBody(bodyDef)

        val fDef = FixtureDef()
        val roundShape = CircleShape()
        //shape.radius = (5/ Game.PPM)
        fDef.shape = roundShape
        body.createFixture(fDef)
    }
}