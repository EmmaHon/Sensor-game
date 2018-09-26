package com.example.anni.riggedpongsensorproject

import android.content.res.Resources
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas

class GameScreen(mGame: RiggedPong): Screen {

    private val game: RiggedPong = mGame
    private val spriteBatch = mGame.batch
    // The camera ensures we can render using our target res of 1080 x 1920(?)
    // pixels no matter what the screen res is
    private val camera = OrthographicCamera()
    private val textureAtlas = TextureAtlas("rp_sprites.pack")
    private val backgroundImage = Texture(Gdx.files.internal("rigged_pong_mockup.png"))
    private val backgroundSprite = Sprite(backgroundImage)
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels.toFloat()
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels.toFloat()

    //private var objectBall = GameObjectBall()
    private var score = 0
    private var highScore = 0
    private var rounds = 3


    init {
        camera.setToOrtho(false, screenWidth, screenHeight)
    }

    fun getAtlas(): TextureAtlas {
        return textureAtlas
    }

    private fun renderBackground() {
        //backgroundSprite.draw(spriteBatch)
        spriteBatch.draw(backgroundImage, 0f, 0f, screenWidth, screenHeight)
    }

    override fun hide() {}

    override fun show() {}

    override fun render(delta: Float) {
        // tell the camera to update its matrices.
        camera.update()
        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.projectionMatrix = camera.combined

        game.batch.begin()
        // Drawing goes here!
        renderBackground()
        game.batch.end()
    }

    override fun pause() {

    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
        backgroundImage.dispose()
        //this.dispose()
    }
}