package com.example.anni.riggedpongsensorproject

import android.content.res.Resources
import android.util.Log
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*


class GameScreen(mGame: RiggedPong): Screen {

    private val game: RiggedPong = mGame
    private val spriteBatch = mGame.batch
    // The camera ensures we can render using our target res of 1080 x 1920(?)
    // pixels no matter what the screen res is
    private val camera = OrthographicCamera()
    val textureAtlas = TextureAtlas("rp_sprites.atlas")
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels.toFloat()
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels.toFloat()

    //Box2D variables
    private val world = World(Vector2(0f,0f), true)
    private val b2dr = Box2DDebugRenderer()

   /* private val bdef = BodyDef()
    private val shape = PolygonShape()
    private val fdef = FixtureDef()
    private lateinit var body: Body*/

    private val playerBall = GameObjectBall(world, this)
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
        game.batch.end()
    }

    override fun pause() {

    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
        //this.dispose()
    }
}