package com.example.anni.riggedpongsensorproject.managers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion

class AssetManager {

    companion object {

        private lateinit var gameScreenAtlas: TextureAtlas
        lateinit var ball: TextureRegion
        lateinit var paddle: TextureRegion
        lateinit var backGround: TextureRegion
        lateinit var roundIndicator: Texture

        private fun loadTexture(file: String): Texture {
            return Texture(Gdx.files.internal(file))
        }

        private fun loadTextureAtlas(file: String): TextureAtlas {
            return TextureAtlas(file)
        }

        fun load() {
            gameScreenAtlas = loadTextureAtlas("rp_sprites.atlas")
            ball = gameScreenAtlas.findRegion("RP_Asset_Ball")
            paddle = gameScreenAtlas.findRegion("RP_Asset_Bat_LEFT")
            backGround = gameScreenAtlas.findRegion("RP_Asset_Play_Area")
            roundIndicator = loadTexture("rp_ui_round.png")

        }

        fun disposeTextures() {
            roundIndicator.dispose()
            gameScreenAtlas.dispose()
        }
    }
}