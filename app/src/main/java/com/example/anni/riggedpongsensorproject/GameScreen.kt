package com.example.anni.riggedpongsensorproject

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*

class GameScreen(mGame: RiggedPong, activity: Activity): Screen, SensorEventListener {

    override fun onSensorChanged(event: SensorEvent?) {}
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private val game: RiggedPong = mGame
    private val spriteBatch = mGame.batch
    // Camera
    private val camera = OrthographicCamera()
    // Sprites
    private val textureAtlas = TextureAtlas("rp_sprites.atlas")
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels.toFloat()
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels.toFloat()
    // Create our Sensor Manager
    private var SM= activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    // Accelerometer Sensor
    private var sensor: Sensor? = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    //Box2D variables
    private val world = World(Vector2(0f,0f), true)
    private val b2dr = Box2DDebugRenderer()

    private val playerBall = GameObjectBall(world, this)
    private var currentScore = 0
    private var currentHighScore = 0
    private var rounds = 3


    init {
        camera.setToOrtho(false, screenWidth, screenHeight)

        SM.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);

    }

    fun getAtlas(): TextureAtlas {
        return textureAtlas
    }

    private fun renderBackground() {
        val backgroundTexture = getAtlas().findRegion("RP_Asset_Play_Area")
        spriteBatch.draw(backgroundTexture, 0f, 0f, screenWidth, screenHeight)
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
        playerBall.draw(spriteBatch)
        playerBall.moveBall(delta)
        game.batch.end()
    }

    override fun pause() {}

    override fun resume() {}

    override fun resize(width: Int, height: Int) {}

    override fun dispose() {
        //this.dispose()
    }
}