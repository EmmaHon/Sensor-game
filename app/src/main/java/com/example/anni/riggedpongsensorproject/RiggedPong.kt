package com.example.anni.riggedpongsensorproject

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class RiggedPong : Game() {

    lateinit var batch: SpriteBatch // only one Spritebatch
    lateinit var font: BitmapFont

    override fun create() {
        batch = SpriteBatch()
        font = BitmapFont()
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.begin()
        if (font.scaleX < 30) {
            font.data.scale(1.1f)
        }

        font.draw(batch, "Hello", 50f, 400f)
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
    }
}
