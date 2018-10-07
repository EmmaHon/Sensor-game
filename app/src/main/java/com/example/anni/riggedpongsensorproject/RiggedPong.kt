package com.example.anni.riggedpongsensorproject

import android.app.Activity
import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.example.anni.riggedpongsensorproject.screens.GameScreen

class RiggedPong(activity: Activity) : Game() {

    companion object {
        const val APP_TITLE = "rigged pong"
        const val APP_VERSION = 0.1
        const val SCALE = 2f
        const val DENSITY = 1f
        const val APP_FPS = 60f
        const val PPM = 32f //Pixel Per Meter
    }

    // batches
    private lateinit var batch: SpriteBatch

    fun getSpriteBatch(): SpriteBatch {
        return batch
    }

    // called when application is created
    override fun create() {
        batch = SpriteBatch()
        this.setScreen(GameScreen(this))
    }

    override fun render() {
        super.render()
    }

    override fun dispose() {
        this.getScreen().dispose()
        batch.dispose()
    }

}
