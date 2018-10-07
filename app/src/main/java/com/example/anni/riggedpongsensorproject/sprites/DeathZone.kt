package com.example.anni.riggedpongsensorproject.sprites

import com.badlogic.gdx.physics.box2d.*
import com.example.anni.riggedpongsensorproject.RiggedPong
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.DENSITY
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.SCALE
import com.example.anni.riggedpongsensorproject.utils.ObjectBits

class DeathZone(mWorld: World, width: Float, height: Float, xPos: Float, yPos: Float) {

    private val world = mWorld
    private lateinit var body: Body
    private val zoneWidth = width
    private val zoneHeight = height
    private val zoneX = xPos
    private val zoneY = yPos

    init {
        createDeathZone(ObjectBits.DEATH.bits, ObjectBits.BALL.bits, 0)
    }

    fun getDeathZoneBody(): Body {
        return body
    }

    private fun createDeathZone(cBits: Short, mBits: Short, gIndex: Short) {
        val bDef = BodyDef()
        bDef.type = BodyDef.BodyType.StaticBody
        bDef.position.set(zoneX/ RiggedPong.PPM / SCALE, zoneY/ RiggedPong.PPM / SCALE)
        body = world.createBody(bDef)
        val deathZoneShape = PolygonShape()
        deathZoneShape.setAsBox(zoneWidth/ RiggedPong.PPM / 4f, zoneHeight/ RiggedPong.PPM / 4f)
        var fDef = FixtureDef()
        fDef.shape = deathZoneShape
        fDef.density = 0f
        fDef.filter.categoryBits = cBits // is a property
        fDef.filter.maskBits = mBits // collides with a property
        fDef.filter.groupIndex = gIndex
        body.createFixture(fDef)
        deathZoneShape.dispose()
    }
}