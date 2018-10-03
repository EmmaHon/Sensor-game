package com.example.anni.riggedpongsensorproject.screens

import android.content.res.Resources
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.example.anni.riggedpongsensorproject.sprites.GameObjectBall
import com.example.anni.riggedpongsensorproject.RiggedPong
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.APP_FPS
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.APP_HEIGHT
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.APP_WIDTH
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.PPM
import com.example.anni.riggedpongsensorproject.sprites.Bat

class GameScreen(mGame: RiggedPong): Screen {

    // batch
    private val batch = mGame.getSpriteBatch()
    // camera
    private val camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    // sprites
    private val textureAtlas = TextureAtlas("rp_sprites.atlas")
    // screen size
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels.toFloat()
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels.toFloat()
    // box2d
    private lateinit var world: World
    private val b2Debug = Box2DDebugRenderer() // render body objects for debugging
    // player
    private lateinit var playerBall: GameObjectBall
    // objects
    private lateinit var batLeft: Bat
    private lateinit var batRight: Bat

    // what needs to be in memory, otherwise move to show-method
    init {
        // sets a viewport according to given width and height
        camera.setToOrtho(false, screenWidth, screenHeight)
    }

    // public functions
    fun getWorld(): World {
        return world
    }

    fun getAtlasObjects(): TextureAtlas {
        return textureAtlas
    }

    fun update(delta: Float) {
        // advance the world by the amount of time that has elapsed
        world.step(1/APP_FPS, 6, 2)
        Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        updateCamera()
    }

    fun updateCamera() {
        camera.update()
    }

    // functions from Screen
    override fun show() {
        world = World(Vector2(0f,0f), true)
        createWalls()
        // player
        playerBall = GameObjectBall(this)
        // objects
        batLeft = Bat(this, 0)
        batRight = Bat(this, 1)
        batch.projectionMatrix = camera.combined
    }

    //called ~60 times per second, game logic updates performed here
    override fun render(delta: Float) {
        update(delta)
        batch.begin()
        //renderBackground()
        batch.draw(playerBall, playerBall.x, playerBall.y)
        renderBats()
        //playerBall.moveBall(delta)
        batch.end()
        b2Debug.render(world, camera.combined.cpy().scl(PPM))
    }

    override fun hide() {}

    // called when home btn is pressed on android or when receiving a call
    // save game state here
    override fun pause() {}

    // called when app resumes from a paused state
    override fun resume() {}

    // called when screen in resized
    override fun resize(width: Int, height: Int) {
        camera.setToOrtho(false, width/ RiggedPong.SCALE, height/ RiggedPong.SCALE)
    }

    // called when app is destroyed
    override fun dispose() {
        textureAtlas.dispose()
    }

    // private functions
    private fun renderBackground() {
        val backgroundTexture = textureAtlas.findRegion("RP_Asset_Play_Area")
        batch.draw(backgroundTexture, 0f, 0f, screenWidth, screenHeight)
    }

    private fun renderBats() {
        batch.draw(batLeft, batLeft.x, batLeft.y)
        batch.draw(batRight, batRight.x, batRight.y)
    }

    private fun createWalls() {
        val bDef = BodyDef()
        bDef.type = BodyDef.BodyType.StaticBody
        val body = world.createBody(bDef)
        val shape = createChainShape()
        body.createFixture(shape, 0f)
        shape.dispose()
    }

    private fun createChainShape(): ChainShape {
        val chainShape = ChainShape()
        // create a full "box" along the screen sides
        val vertices = arrayOfNulls<Vector2>(5)
        val adjustFactor = 2f
        vertices[0] = Vector2(1f/ PPM/ adjustFactor, 1f/ PPM/ adjustFactor)
        vertices[1] = Vector2(camera.viewportWidth/ PPM/ adjustFactor, 1f/ PPM/ adjustFactor)
        vertices[2] = Vector2(camera.viewportWidth/ PPM/ adjustFactor, camera.viewportHeight/ PPM/ adjustFactor)
        vertices[3] = Vector2(1f/ PPM/ adjustFactor, camera.viewportHeight/ PPM/ adjustFactor)
        vertices[4] = Vector2(1f/ PPM/ adjustFactor, 1f/ PPM/ adjustFactor)
        chainShape.createChain(vertices)
        return chainShape
    }
}