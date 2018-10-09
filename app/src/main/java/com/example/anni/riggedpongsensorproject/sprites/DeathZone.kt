package com.example.anni.riggedpongsensorproject.sprites

import com.badlogic.gdx.physics.box2d.*
import com.example.anni.riggedpongsensorproject.RiggedPong
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.SCALE

class DeathZone(mWorld: World, width: Float, height: Float, xPos: Float, yPos: Float) {

    private val world = mWorld
    private lateinit var body: Body
    private val zoneWidth = width
    private val zoneHeight = height
    private val zoneX = xPos
    private val zoneY = yPos

    init {
        createDeathZone()
    }

    fun getDeathZoneBody(): Body {
        return body
    }

    private fun createDeathZone() {
        val bDef = BodyDef()
        bDef.type = BodyDef.BodyType.StaticBody
        bDef.position.set(zoneX/ RiggedPong.PPM / SCALE, zoneY/ RiggedPong.PPM / SCALE)
        body = world.createBody(bDef)
        val deathZoneShape = PolygonShape()
        deathZoneShape.setAsBox(zoneWidth/ RiggedPong.PPM / 4f, zoneHeight/ RiggedPong.PPM / 4f)
        var fDef = FixtureDef()
        fDef.shape = deathZoneShape
        fDef.density = 0f
        body.createFixture(fDef)
        deathZoneShape.dispose()
    }
}