package com.example.anni.riggedpongsensorproject.screens

import android.content.res.Resources
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef
import com.example.anni.riggedpongsensorproject.sprites.GameObjectBall
import com.example.anni.riggedpongsensorproject.RiggedPong
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.APP_FPS
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.PPM
import com.example.anni.riggedpongsensorproject.sprites.DeathZone
import com.example.anni.riggedpongsensorproject.sprites.Paddle

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
    private lateinit var paddleLeft: Paddle
    private lateinit var paddleRight: Paddle
    private lateinit var deathZoneLeft: DeathZone
    private lateinit var deathZoneRight: DeathZone

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

        //get mouse position - move paddle
        //var touchPositionToWorld = -(Gdx.input.y) - camera.viewportHeight / PPM
        //paddleLeft.getPaddleBody().setTransform(paddleLeft.getPaddleBody().position.x, touchPositionToWorld, paddleLeft.getPaddleBody().angle)
        //clear the screen
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
        setupGameArea()
        batch.projectionMatrix = camera.combined
    }

    //called ~60 times per second, game logic updates performed here
    override fun render(delta: Float) {
        update(delta)
        batch.begin()
        renderBackground()
        batch.draw(playerBall, playerBall.x, playerBall.y)
        renderBats()
        playerBall.moveBall(delta)
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
        batch.draw(paddleLeft, paddleLeft.x, paddleLeft.y)
        batch.draw(paddleRight, paddleRight.x, paddleRight.y)
    }

    private fun setupGameArea() {
        createWalls()
        // player
        playerBall = GameObjectBall(this, camera.viewportWidth/2, camera.viewportHeight/2)
        // objects
        paddleLeft = Paddle(this, 200f, camera.viewportHeight/2,0)
        paddleRight = Paddle(this,  camera.viewportWidth - 200f, camera.viewportHeight/2,1)
        deathZoneLeft = DeathZone(world, 60f, screenHeight,1f, camera.viewportHeight/2f)
        deathZoneRight = DeathZone(world, 60f, screenHeight, camera.viewportWidth - 1f, camera.viewportHeight/2)

        createJoint(deathZoneLeft.getDeathZoneBody(), paddleLeft.getPaddleBody(), camera.viewportHeight/ 2, - camera.viewportHeight/ 2)
        createJoint(deathZoneRight.getDeathZoneBody(), paddleRight.getPaddleBody(), camera.viewportHeight/ 2, - camera.viewportHeight/ 2)
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

    private fun createJoint(bodyA: Body, bodyB: Body, upperLimit: Float, lowerLimit: Float): Joint {
        var pDef = PrismaticJointDef()
        pDef.bodyA = bodyA
        pDef.bodyB = bodyB
        // no colliding between two bodies
        pDef.collideConnected = false
        pDef.enableLimit = true
        pDef.localAnchorA.set(16f/ PPM, 0f)
        pDef.upperTranslation = upperLimit
        pDef.lowerTranslation = lowerLimit
        return world.createJoint(pDef)
    }
}