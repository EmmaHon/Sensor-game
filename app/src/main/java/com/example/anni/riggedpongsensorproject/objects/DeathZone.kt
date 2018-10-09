package com.example.anni.riggedpongsensorproject.objects

import com.badlogic.gdx.physics.box2d.*
import com.example.anni.riggedpongsensorproject.RiggedPong
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.SCALE

class DeathZone(private val world: World, private val width: Float, private val height: Float,
                private val xPos: Float, private val yPos: Float) {

    private lateinit var body: Body

    init {
        createDeathZone()
    }

    fun getDeathZoneBody(): Body {
        return body
    }

    private fun createDeathZone() {
        val bDef = BodyDef()
        bDef.type = BodyDef.BodyType.StaticBody
        bDef.position.set(xPos/ RiggedPong.PPM / SCALE, yPos/ RiggedPong.PPM / SCALE)
        body = world.createBody(bDef)
        val deathZoneShape = PolygonShape()
        deathZoneShape.setAsBox(width/ RiggedPong.PPM / 4f, height/ RiggedPong.PPM / 4f)
        val fDef = FixtureDef()
        fDef.shape = deathZoneShape
        fDef.density = 0f
        body.createFixture(fDef)
        deathZoneShape.dispose()
    }
}