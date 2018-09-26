package com.example.anni.riggedpongsensorproject

import android.content.pm.ActivityInfo
import android.content.res.Resources
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture

class GameScreen(mGame: RiggedPong): Screen {

    private val game: RiggedPong = mGame
    // The camera ensures we can render using our target res of 1080 x 1920(?)
    // pixels no matter what the screen res is
    private val camera = OrthographicCamera()
    private val backgroundImage = Texture(Gdx.files.internal("rigged_pong_mockup.png"))
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels.toFloat()
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels.toFloat()

    init {
        camera.setToOrtho(false, screenWidth, screenHeight)
    }

    override fun hide() {
    }

    override fun show() {

    }

    override fun render(delta: Float) {
        // clear the screen with a dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // tell the camera to update its matrices.
        camera.update()
        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.projectionMatrix = camera.combined

        game.batch.begin()
        // Drawing goes here!
        game.batch.draw(backgroundImage, 0f, 0f, screenWidth, screenHeight)
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