package com.example.anni.riggedpongsensorproject.managers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
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
        lateinit var paddleLeftSound: Sound
        lateinit var paddleRightSound: Sound
        lateinit var deathZoneHitSound: Sound

        private fun loadTexture(file: String): Texture {
            return Texture(Gdx.files.internal(file))
        }

        private fun loadTextureAtlas(file: String): TextureAtlas {
            return TextureAtlas(file)
        }

        private fun loadSound(file: String): Sound {
            return (Gdx.audio.newSound(Gdx.files.internal(file)))
        }

        fun load() {
            gameScreenAtlas = loadTextureAtlas("rp_sprites.atlas")
            ball = gameScreenAtlas.findRegion("RP_Asset_Ball")
            paddle = gameScreenAtlas.findRegion("RP_Asset_Bat_LEFT")
            backGround = gameScreenAtlas.findRegion("RP_Asset_Play_Area")
            roundIndicator = loadTexture("rp_ui_round.png")
            paddleLeftSound = loadSound("sounds/paddle_hit_1.mp3")
            paddleRightSound = loadSound("sounds/paddle_hit_2.mp3")
            deathZoneHitSound = loadSound("sounds/deathzone_hit.mp3")
        }

        fun dispose() {
            roundIndicator.dispose()
            gameScreenAtlas.dispose()
            paddleRightSound.dispose()
            paddleLeftSound.dispose()
            deathZoneHitSound.dispose()
        }
    }
}