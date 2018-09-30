package com.example.anni.riggedpongsensorproject

import android.app.Activity
import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class RiggedPong(activity: Activity) : Game() {

    lateinit var batch: SpriteBatch // only one Spritebatch
    val mActivity = activity

    companion object {
        val PPM = 100
    }

    override fun create() {
        batch = SpriteBatch()
        this.setScreen(GameScreen(this, mActivity))
    }

    override fun render() {
        super.render()
    }

    override fun dispose() {
        this.getScreen().dispose()
        batch.dispose()
    }
}
