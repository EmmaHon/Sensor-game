package com.example.anni.riggedpongsensorproject

import android.app.Activity
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.scenes.scene2d.ui.Label


class RiggedPong(activity: Activity) : Game() {

    lateinit var batch: SpriteBatch // only one Spritebatch
    val mActivity = activity
    //lateinit var customFont: BitmapFont
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
