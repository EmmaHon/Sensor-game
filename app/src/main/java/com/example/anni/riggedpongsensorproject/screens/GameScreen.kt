package com.example.anni.riggedpongsensorproject.screens

import android.content.res.Resources
import android.util.Log
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef
import com.example.anni.riggedpongsensorproject.sprites.GameObjectBall
import com.example.anni.riggedpongsensorproject.RiggedPong
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.APP_FPS
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.PPM
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.SCALE
import com.example.anni.riggedpongsensorproject.listeners.BallContactListener
import com.example.anni.riggedpongsensorproject.sprites.DeathZone
import com.example.anni.riggedpongsensorproject.sprites.Paddle
import com.example.anni.riggedpongsensorproject.utils.GameState

class GameScreen(pongGame: RiggedPong, pongFont: BitmapFont) : Screen {

    private val game = pongGame
    private val font = pongFont
    private val batch = pongGame.getSpriteBatch()
    private val textureAtlas = TextureAtlas("rp_sprites.atlas")
    private val screenWidth = Gdx.graphics.width.toFloat()
    private val screenHeight = Gdx.graphics.height.toFloat()
    private val camera = OrthographicCamera(screenWidth, screenHeight)
    // box2d
    private lateinit var world: World
    private val b2Debug = Box2DDebugRenderer() // render body objects for debugging
    private lateinit var playerBall: GameObjectBall
    lateinit var paddleLeft: Paddle
    lateinit var paddleRight: Paddle
    private lateinit var deathZoneLeft: DeathZone
    private lateinit var deathZoneRight: DeathZone
    private var gameState = GameState.COUNTDOWN
    private var startTime = 0f
    var score = 0
    var rounds = 3

    // what needs to be in memory, otherwise move to show-method
    init {
        // sets a viewport according to given width and height
        camera.setToOrtho(false, screenWidth, screenHeight)
        Log.d("DEBUG1", "ROUNDS: $rounds")
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

    fun setGameState(state: GameState) {
        startTime = 0f
        gameState = state
    }

    fun resetPlayArea() {
        playerBall.setCenterDimensions()
        resetObjectPositions()
        setGameState(GameState.RESET)
    }

    fun update(delta: Float) {
        // advance the world by the amount of time that has elapsed
        world.step(1 / APP_FPS, 6, 2)
        //setObjectPositions
        playerBall.moveBall(delta, paddleLeft, paddleRight)
        paddleLeft.movePaddle(delta)
        paddleRight.movePaddle(delta)
        //clear the screen
        Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.update()
    }

    // functions from Screen
    override fun show() {
        world = World(Vector2(0f, 0f), true)
        setupPlayArea()
        setOnContactListener()
        batch.projectionMatrix = camera.combined
    }

    //called ~60 times per second, game logic updates performed here
    override fun render(delta: Float) {
        startTime += delta
        when (gameState) {
            GameState.COUNTDOWN -> {
                renderAll()
                if (startTime > 2) setGameState(GameState.PLAY)
            }
            GameState.PLAY -> {
                update(delta)
                renderAll()
            }
            GameState.RESET -> {
                renderAll()
                if (startTime > 1) setGameState(GameState.PLAY)
            }
            GameState.GAME_OVER -> {
                Log.d("DEBUG", "game over!")
            }
        }
        b2Debug.render(world, camera.combined.cpy().scl(PPM))
    }

    override fun hide() {}

    // called when home btn is pressed on android or when receiving a call
    // save game state here
    override fun pause() {}

    // called when app resumes from a paused state
    override fun resume() {}

    // called when screen is resized
    override fun resize(width: Int, height: Int) {
        camera.setToOrtho(false, width / RiggedPong.SCALE, height / RiggedPong.SCALE)
    }

    // called when app is destroyed
    override fun dispose() {
        textureAtlas.dispose()
    }

    private fun renderRounds() {
        val roundUITexture1 = Texture(Gdx.files.internal("rp_ui_round.png"))
        when (rounds) {
            3 -> {
                batch.draw(roundUITexture1, (camera.viewportWidth / 2f - 30f) * SCALE - roundUITexture1.width / 2f,
                        (camera.viewportHeight - 45f) * SCALE)
                batch.draw(roundUITexture1, (camera.viewportWidth / 2f) * SCALE - roundUITexture1.width / 2f,
                        (camera.viewportHeight - 45f) * SCALE)
                batch.draw(roundUITexture1, (camera.viewportWidth / 2f + 30f) * SCALE - roundUITexture1.width / 2f,
                        (camera.viewportHeight - 45f) * SCALE)
            }
            2 -> {
                batch.draw(roundUITexture1, (camera.viewportWidth / 2f + 30f) * SCALE - roundUITexture1.width / 2f,
                        (camera.viewportHeight - 45f) * SCALE)
                batch.draw(roundUITexture1, (camera.viewportWidth / 2f) * SCALE - roundUITexture1.width / 2f,
                        (camera.viewportHeight - 45f) * SCALE)
            }
            1 -> {
                batch.draw(roundUITexture1, (camera.viewportWidth / 2f + 30f) * SCALE - roundUITexture1.width / 2f,
                        (camera.viewportHeight - 45f) * SCALE)
            }
            else -> {
                // rounds = 0, no need to render anything
                Log.d("DEBUG", "no rounds left")
            }
        }
    }

    private fun setOnContactListener() {
        // collision detections
        if (world != null) {
            world.setContactListener(object : BallContactListener(this, playerBall,
                    paddleLeft, paddleRight, deathZoneLeft, deathZoneRight) {})
        }
    }

    private fun renderAll() {
        batch.begin()
        renderBackground()
        renderBats()
        renderRounds()
        when {
            score < 10 -> font.draw(batch, score.toString(), (camera.viewportWidth / 2f - 14f) * SCALE, 120f)
            score < 100 -> font.draw(batch, score.toString(), (camera.viewportWidth / 2f - 28f) * SCALE, 120f)
            score < 1000 -> font.draw(batch, score.toString(), (camera.viewportWidth / 2f - 42f) * SCALE, 120f)
            score < 10000 -> font.draw(batch, score.toString(), (camera.viewportWidth / 2f - 56f) * SCALE, 120f)
        }
        if (gameState == GameState.COUNTDOWN && startTime.toInt() <= 2) {
            val countDown = 3 - startTime.toInt()
            font.draw(batch, countDown.toString(), (camera.viewportWidth / 2f + 20f) * SCALE, (camera.viewportHeight / 2f + 50f) * SCALE)
        }
        batch.draw(playerBall.getBallSprite(), playerBall.getBallSprite().x,
                playerBall.getBallSprite().y)
        batch.end()
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

    private fun setupPlayArea() {
        //createWalls()
        // paddles
        paddleLeft = Paddle(this, 220f, camera.viewportHeight / 2, 0)
        paddleRight = Paddle(this, camera.viewportWidth - 220f, camera.viewportHeight / 2f, 1)
        // deathZones
        deathZoneLeft = DeathZone(world, 330f, camera.viewportHeight - 250f, 1f, camera.viewportHeight / 2f)
        deathZoneRight = DeathZone(world, 330f, camera.viewportHeight - 250f, camera.viewportWidth - 1f, camera.viewportHeight / 2)
        // Joint between paddle and deathZone
        createJoint(deathZoneLeft.getDeathZoneBody(), paddleLeft.getPaddleBody(), camera.viewportHeight / 2,
                -camera.viewportHeight / 2, Vector2(110f / PPM, 0f), Vector2(0f, 0f))
        createJoint(deathZoneRight.getDeathZoneBody(), paddleRight.getPaddleBody(), camera.viewportHeight / 2,
                -camera.viewportHeight / 2, Vector2(-110f / PPM, 0f), Vector2(0f, 0f))
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
        vertices[0] = Vector2(20f / PPM / adjustFactor, 60f / PPM / adjustFactor)
        vertices[1] = Vector2((camera.viewportWidth - 20f) / PPM / adjustFactor, 60f / PPM / adjustFactor)
        vertices[2] = Vector2((camera.viewportWidth - 20f) / PPM / adjustFactor, (camera.viewportHeight - 60f) / PPM / adjustFactor)
        vertices[3] = Vector2(20f / PPM / adjustFactor, (camera.viewportHeight - 60f) / PPM / adjustFactor)
        vertices[4] = Vector2(20f / PPM / adjustFactor, 60f / PPM / adjustFactor)
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

    private fun resetObjectPositions() {
        playerBall.getBallSprite().setPosition(
                (playerBall.getBallBody().position.x * PPM * SCALE) - playerBall.getBallSprite().width / 2f,
                (playerBall.getBallBody().position.y * PPM * SCALE) - playerBall.getBallSprite().height / 2f)
        playerBall.getBallBody().setTransform((playerBall.getBallSprite().x) / PPM / SCALE,
                playerBall.getBallSprite().y / PPM / SCALE,
                playerBall.getBallBody().angle)
        /*   paddleLeft.getPaddleSprite().setPosition(
                   (paddleLeft.getPaddleBody().position.x * PPM * SCALE) - paddleLeft.getPaddleSprite().width/2f,
                   (paddleLeft.getPaddleBody().position.y * PPM * SCALE)- paddleLeft.getPaddleSprite().height/2f)
           paddleRight.getPaddleSprite().setPosition(
                   (paddleRight.getPaddleBody().position.x * PPM * SCALE) - paddleRight.getPaddleSprite().width/2f,
                   (paddleRight.getPaddleBody().position.y * PPM * SCALE)- paddleRight.getPaddleSprite().height/2f)*/
    }
}