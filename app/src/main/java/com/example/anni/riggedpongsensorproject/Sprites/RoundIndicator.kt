package com.example.anni.riggedpongsensorproject.Sprites

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.example.anni.riggedpongsensorproject.GameScreen

class RoundIndicator(mGameScreen: GameScreen): Sprite() {

    private val gameScreen = mGameScreen
    private val roundTextureRegion = gameScreen.getAtlasUI().findRegion("RP_UI_Round")
    private val roundTextureHeight = roundTextureRegion.originalHeight.toFloat()
    private val roundTextureWidth = roundTextureRegion.originalWidth.toFloat()

    init {
        // To center it, you need to subtract half of the textures width from the x,
        // and half of the textures height from the y coordinate. Something along these lines:
        // position to top and center of the screen
        setBounds(912f, 993f, roundTextureWidth, roundTextureHeight)
        setRegion(roundTextureRegion)
    }

    fun onRoundChange() {

    }

    override fun draw(batch: Batch?) {
        super.draw(batch)

    }
}