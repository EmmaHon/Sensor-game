package com.example.anni.riggedpongsensorproject.screens

import android.content.res.Resources
import android.util.Log
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef
import com.example.anni.riggedpongsensorproject.sprites.GameObjectBall
import com.example.anni.riggedpongsensorproject.RiggedPong
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.APP_FPS
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.PPM
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.SCALE
import com.example.anni.riggedpongsensorproject.sprites.DeathZone
import com.example.anni.riggedpongsensorproject.sprites.Paddle

class GameScreen(mGame: RiggedPong): Screen {

    // batch
    private val batch = mGame.getSpriteBatch()
    // textures
    private val textureAtlas = TextureAtlas("rp_sprites.atlas")
    // screen size
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels.toFloat()
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels.toFloat()
    // camera
    private val camera = OrthographicCamera(screenWidth, screenHeight)
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
    private var score = 0
    private var round = 3

    var drawSprite = true

    // what needs to be in memory, otherwise move to show-method
    init {
        // sets a viewport according to given width and height
        camera.setToOrtho(false, screenWidth, screenHeight)
        Log.d("DEBUG1", "ROUNDS: $round")
    }

    // public functions
    fun getWorld(): World {
        return world
    }

    fun getCamera(): OrthographicCamera {
        return camera
    }

    fun getAtlasObjects(): TextureAtlas {
        return textureAtlas
    }

    fun update(delta: Float) {
        // advance the world by the amount of time that has elapsed
        world.step(1/APP_FPS, 6, 2)
        //setObjectPositions
        playerBall.moveBall(delta)
        paddleLeft.movePaddle(delta)
        paddleRight.movePaddle(delta)
        //clear the screen
        Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        //camera.update()
    }

    fun resetPlayArea() {

    }

    fun setOnContactListener() {
        // collision detection
        if (world != null) {
            world.setContactListener(object : ContactListener {
                override fun beginContact(contact: Contact?) {
                    val fixA = contact!!.fixtureA.body
                    val fixB = contact.fixtureB.body

                    // collision with deathzones, decrease rounds here and reset arena
                    if (fixA == deathZoneLeft.getDeathZoneBody() || fixA == deathZoneRight.getDeathZoneBody()
                            && fixB == playerBall.getBallBody()) {
                        Log.d("DEBUG1", "CONTACT with death zone!")
                        --round
                        Log.d("DEBUG2", "rounds: $round")
                        resetPlayArea()
                    }
                    // collision with paddles, increase score here
                    if (fixA == paddleLeft.getPaddleBody() ||  fixA == paddleRight.getPaddleBody()
                            && fixB == playerBall.getBallBody()) {
                        Log.d("DEBUG3", "CONTACT with paddle!")
                        score += 10
                        Log.d("DEBUG4", "score: $score")
                    }
                }
                override fun endContact(contact: Contact?) {
                    val fixA = contact!!.fixtureA
                    val fixB = contact.fixtureB
                }
                override fun preSolve(contact: Contact?, oldManifold: Manifold?) {}
                override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {}
            })
        }
    }

    // functions from Screen
    override fun show() {
        world = World(Vector2(0f,0f), true)
        setupGameArea()
        setOnContactListener()
        batch.projectionMatrix = camera.combined
    }

    //called ~60 times per second, game logic updates performed here
    override fun render(delta: Float) {
        update(delta)
        batch.begin()
        if (drawSprite) {
            renderBackground()
            renderBats()
            batch.draw(playerBall.getBallSprite(), playerBall.getBallSprite().x,
                       playerBall.getBallSprite().y)
        }
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
    private fun setObjectPositions() {
        playerBall.getBallSprite().setPosition(
                (playerBall.getBallBody().position.x * PPM * SCALE) - playerBall.getBallSprite().width/2f,
                (playerBall.getBallBody().position.y * PPM * SCALE)- playerBall.getBallSprite().height/2f)
        paddleLeft.getPaddleSprite().setPosition(
                (paddleLeft.getPaddleBody().position.x * PPM * SCALE) - paddleLeft.getPaddleSprite().width/2f,
                (paddleLeft.getPaddleBody().position.y * PPM * SCALE)- paddleLeft.getPaddleSprite().height/2f)
        paddleRight.getPaddleSprite().setPosition(
                (paddleRight.getPaddleBody().position.x * PPM * SCALE) - paddleRight.getPaddleSprite().width/2f,
                (paddleRight.getPaddleBody().position.y * PPM * SCALE)- paddleRight.getPaddleSprite().height/2f)
    }

    private fun renderBackground() {
        val backgroundTexture = textureAtlas.findRegion("RP_Asset_Play_Area")
        batch.draw(backgroundTexture, 0f, 0f, screenWidth, screenHeight)
    }

    private fun renderBats() {
        batch.draw(paddleLeft.getPaddleSprite(), paddleLeft.getPaddleSprite().x,
                   paddleLeft.getPaddleSprite().y)
        batch.draw(paddleRight.getPaddleSprite(), paddleRight.getPaddleSprite().x,
                   paddleRight.getPaddleSprite().y)
    }

    private fun setupGameArea() {
        createWalls()
        // paddles
        paddleLeft = Paddle(this, 135f, camera.viewportHeight/2,0)
        paddleRight = Paddle(this,  camera.viewportWidth - 135f,camera.viewportHeight/2f,1)
        // deathZones
        deathZoneLeft = DeathZone(world, 160f, camera.viewportHeight - 200f,1f, camera.viewportHeight/2f)
        deathZoneRight = DeathZone(world, 160f, camera.viewportHeight - 200f, camera.viewportWidth - 1f, camera.viewportHeight/2)
        // Joint between paddle and deathZone
        createJoint(deathZoneLeft.getDeathZoneBody(), paddleLeft.getPaddleBody(), camera.viewportHeight/ 2,
                - camera.viewportHeight/ 2,  Vector2(67f/ PPM, 0f), Vector2(0f, 0f))
        createJoint(deathZoneRight.getDeathZoneBody(), paddleRight.getPaddleBody(), camera.viewportHeight/ 2,
                - camera.viewportHeight/ 2, Vector2(-67f/ PPM, 0f), Vector2(0f, 0f))
        // player
        playerBall = GameObjectBall(this)
    }

    private fun createWalls() {
        val bDef = BodyDef()
        bDef.type = BodyDef.BodyType.StaticBody
        val body = world.createBody(bDef)
        val shape = createChainShape()
        var fDef = FixtureDef()
        fDef.shape = shape
        fDef.density = 0f
        val cBits: Short = 0x1 // is a paddle/wall
        val mBits: Short = 0x2 // collides with player
        val gIndex: Short = 0
        fDef.filter.categoryBits = cBits // is a property
        fDef.filter.maskBits = mBits // collides with a property
        fDef.filter.groupIndex = gIndex
        body.createFixture(fDef)
        shape.dispose()
    }

    private fun createChainShape(): ChainShape {
        val chainShape = ChainShape()
        // create a full "box" along the screen sides
        val vertices = arrayOfNulls<Vector2>(5)
        val adjustFactor = 2f
        vertices[0] = Vector2(20f/ PPM/ adjustFactor, 60f/ PPM/ adjustFactor)
        vertices[1] = Vector2((camera.viewportWidth -20f)/ PPM/ adjustFactor, 60f/ PPM/ adjustFactor)
        vertices[2] = Vector2((camera.viewportWidth -20f)/ PPM/ adjustFactor, (camera.viewportHeight -60f)/ PPM/ adjustFactor)
        vertices[3] = Vector2(20f/ PPM/ adjustFactor, (camera.viewportHeight -60f)/ PPM/ adjustFactor)
        vertices[4] = Vector2(20f/ PPM/ adjustFactor, 60f/ PPM/ adjustFactor)
        chainShape.createChain(vertices)
        return chainShape
    }

    private fun createJoint(bodyA: Body, bodyB: Body, upperLimit: Float, lowerLimit: Float,
                            anchorA: Vector2, anchorB: Vector2): Joint {
        var pDef = PrismaticJointDef()
        pDef.bodyA = bodyA
        pDef.bodyB = bodyB
        // no colliding between the two bodies
        pDef.collideConnected = false
        pDef.enableLimit = true
        pDef.localAnchorA.set(anchorA)
        pDef.localAnchorB.set(anchorB)
        pDef.localAxisA.set(0f, 1f)
        pDef.upperTranslation = upperLimit / PPM
        pDef.lowerTranslation = lowerLimit / PPM
        return world.createJoint(pDef)
    }
}