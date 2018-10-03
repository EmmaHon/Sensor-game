package com.example.anni.riggedpongsensorproject.sprites

import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Shape
import com.badlogic.gdx.physics.box2d.World
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.DENSITY

class DeathZone(mWorld: World, shape: Shape) {

    private val world = mWorld
    private lateinit var body: Body
    private val deathZoneShape = shape

    init {
        createDeathZone()
    }

    private fun createDeathZone() {
        val bDef = BodyDef()
        bDef.type = BodyDef.BodyType.StaticBody
        body = world.createBody(bDef)
        body.createFixture(deathZoneShape, DENSITY).userData = this
        deathZoneShape.dispose()
    }
}