package com.example.anni.riggedpongsensorproject

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.example.anni.riggedpongsensorproject.Sprites.Bat
import com.example.anni.riggedpongsensorproject.Sprites.RoundIndicator
import com.example.anni.riggedpongsensorproject.Sprites.ScoreNumber

class GameScreen(mGame: RiggedPong, activity: Activity): Screen, SensorEventListener {

    override fun onSensorChanged(event: SensorEvent?) {}
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private val game: RiggedPong = mGame
    private val spriteBatch = mGame.batch
    // Camera
    private val camera = OrthographicCamera()
    // Sprites
    private val textureAtlasObjects = TextureAtlas("rp_sprites.atlas")
    private val textureAtlasUI = TextureAtlas("rp_sprites_ui.atlas")
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels.toFloat()
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels.toFloat()
    // Create Sensor Manager
    private var SM= activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    // Accelerometer Sensor
    private var sensor: Sensor? = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    //Box2D variables
    private val world = World(Vector2(0f,0f), true) //world, no gravity
    private val b2dr = Box2DDebugRenderer()

    // Player variables
    private val playerBall = GameObjectBall(this, spriteBatch, world)
    private val batLeft = Bat(this, spriteBatch, world)
    private val batRight = Bat(this, spriteBatch, world)
    private val roundSprite = RoundIndicator(this)
    private val scoreNumbers = ScoreNumber(this)

    private var currentScore = 0
    private var currentHighScore = 0
    private var rounds = 3


    init {
        camera.setToOrtho(false, screenWidth, screenHeight)
        SM.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    fun getAtlasObjects(): TextureAtlas {
        return textureAtlasObjects
    }

    fun getAtlasUI(): TextureAtlas {
        return textureAtlasUI
    }

    private fun renderBackground() {
        val backgroundTexture = getAtlasObjects().findRegion("RP_Asset_Play_Area")
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

        // clear the screen
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

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
        world.dispose()
        this.dispose()
    }
}