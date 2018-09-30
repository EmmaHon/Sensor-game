package com.example.anni.riggedpongsensorproject.Sprites

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.example.anni.riggedpongsensorproject.GameScreen

class ScoreNumber(mGameScreen: GameScreen): Sprite() {

    private val gameScreen = mGameScreen
    private val zeroTextureRegion = gameScreen.getAtlasUI().findRegion("RP_Asset_Number_Zero")
    private val oneTextureRegion = gameScreen.getAtlasUI().findRegion("RP_Asset_Number_One")
    private val twoTextureRegion = gameScreen.getAtlasUI().findRegion("RP_Asset_Number_Two")
    private val threeTextureRegion = gameScreen.getAtlasUI().findRegion("RP_Asset_Number_Three")
    private val fourTextureRegion = gameScreen.getAtlasUI().findRegion("RP_Asset_Number_Four")
    private val fiveTextureRegion = gameScreen.getAtlasUI().findRegion("RP_Asset_Number_Five")
    private val sixTextureRegion = gameScreen.getAtlasUI().findRegion("RP_Asset_Number_Six")
    private val sevenTextureRegion = gameScreen.getAtlasUI().findRegion("RP_Asset_Number_Seven")
    private val eightTextureRegion = gameScreen.getAtlasUI().findRegion("RP_Asset_Number_Eight")
    private val nineTextureRegion = gameScreen.getAtlasUI().findRegion("RP_Asset_Number_Nine")
    private val numberTextureHeight = zeroTextureRegion.originalHeight.toFloat()
    private val numberTextureWidth = zeroTextureRegion.originalWidth.toFloat()

    init {
        // position to top and center of the screen
        setBounds(830f, 898f, numberTextureWidth, numberTextureHeight)
        setRegion(zeroTextureRegion)
    }

    fun setupScoreBoard() {

    }

    fun onScoreChanged() {

    }

    override fun draw(batch: Batch?) {
        super.draw(batch)

    }
}