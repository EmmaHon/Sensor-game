package com.example.anni.riggedpongsensorproject

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class RiggedPong : Game() {

    lateinit var batch: SpriteBatch // only one Spritebatch

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
