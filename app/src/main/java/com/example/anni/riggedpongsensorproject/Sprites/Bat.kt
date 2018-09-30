package com.example.anni.riggedpongsensorproject.Sprites

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Sprite
import com.example.anni.riggedpongsensorproject.GameScreen

class Bat(mGameScreen: GameScreen): Sprite() {

    private val gameScreen = mGameScreen
    private val batTextureRegion = gameScreen.getAtlasObjects().findRegion("RP_Asset_Bat_LEFT")
    private val batTextureHeight = batTextureRegion.originalHeight.toFloat()
    private val batTextureWidth = batTextureRegion.originalWidth.toFloat()

    init {
        // To center it, you need to subtract half of the textures width from the x,
        // and half of the textures height from the y coordinate. Something along these lines:
        //val xBallPosition: Float = (Gdx.graphics.width/ 2f - batTextureWidth/ 2f)
        // position to top and center of the screen
        //setBounds(830f, 898f, batTextureWidth, batTextureHeight)
        setRegion(batTextureRegion)
    }

    fun setPosition(side: String) {
        val yBatPosition: Float = (Gdx.graphics.height/ 2f - batTextureHeight/ 2f)
        if (side == "left") {
            setBounds(200f, yBatPosition, batTextureWidth, batTextureHeight)
        } else {
            setBounds(1525f, yBatPosition, batTextureWidth, batTextureHeight)
        }
    }

    fun createBox2DWall() {

    }
}