package com.example.anni.riggedpongsensorproject

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.example.anni.riggedpongsensorproject.screens.GameScreen

class RiggedPong : Game() {

    companion object {
        const val SCALE = 2f
        const val DENSITY = 1f
        const val APP_FPS = 60f
        const val PPM = 32f //Pixel Per Meter
    }

    private lateinit var batch: SpriteBatch

    fun getSpriteBatch(): SpriteBatch {
        return batch
    }

    // called when application is created
    override fun create() {
        batch = SpriteBatch()
        val font22 = createFont()
        this.setScreen(GameScreen(this, font22))
    }

    override fun dispose() {
        this.getScreen().dispose()
        batch.dispose()
    }

    // private functions
    private fun createFont(): BitmapFont {
        val fontFile = Gdx.files.internal("fonts/pixelite.fnt")
        val generator = FreeTypeFontGenerator(fontFile)
        val fontParam = FreeTypeFontGenerator.FreeTypeFontParameter()
        fontParam.size = 22
        val font = generator.generateFont(fontParam)
        font.data.setScale(7f, 7f)
        generator.dispose()
        return font
    }
}
