package com.example.anni.riggedpongsensorproject.screens

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.provider.SyncStateContract.Helpers.update
import android.support.v4.content.ContextCompat.startActivity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef
import com.example.anni.riggedpongsensorproject.MainActivity
import com.example.anni.riggedpongsensorproject.RiggedPong
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.APP_FPS
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.PPM
import com.example.anni.riggedpongsensorproject.RiggedPong.Companion.SCALE
import com.example.anni.riggedpongsensorproject.managers.AssetManager
import com.example.anni.riggedpongsensorproject.listeners.BallContactListener
import com.example.anni.riggedpongsensorproject.objects.DeathZone
import com.example.anni.riggedpongsensorproject.objects.GameObjectBall
import com.example.anni.riggedpongsensorproject.objects.Paddle
import com.example.anni.riggedpongsensorproject.utils.Constants
import com.example.anni.riggedpongsensorproject.utils.GameState

class GameScreen(private val activity: Activity, private val game: RiggedPong,
                 private val font: BitmapFont) : Screen {

    private val batch = game.getSpriteBatch()
    private val screenWidth = Gdx.graphics.width.toFloat()
    private val screenHeight = Gdx.graphics.height.toFloat()
    private val camera = OrthographicCamera(screenWidth, screenHeight)
    private var gameState = GameState.COUNTDOWN
    private var startTime = 0f
    private lateinit var deathZoneLeft: DeathZone
    private lateinit var deathZoneRight: DeathZone
    private lateinit var world: World
    private lateinit var playerBall: GameObjectBall
    private lateinit var paddleLeft: Paddle
    private lateinit var paddleRight: Paddle
    var score = 0
    var rounds = 3

    init {
        // sets a viewport according to given width and height
        camera.setToOrtho(false, screenWidth, screenHeight)
        AssetManager.load()
    }

    override fun show() {
        world = World(Vector2(0f, 0f), true)
        setupPlayArea()
        setOnContactListener()
        batch.projectionMatrix = camera.combined
    }

    override fun resize(width: Int, height: Int) {
        camera.setToOrtho(false, width/ SCALE, height/ SCALE)
    }

    override fun hide() {}

    override fun pause() {}

    override fun resume() {}

    override fun dispose() {
        AssetManager.dispose()
    }

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
                renderAll()
                if (startTime > 2) gameOver()
            }
        }
    }

    // public functions
    fun getWorld(): World {
        return world
    }

    fun getCamera(): OrthographicCamera {
        return camera
    }

    fun resetPlayArea() {
        playerBall.setCenterDimensions()
        resetObjectPositions()
        if (rounds < 0) {
            setGameState(GameState.GAME_OVER)
        } else {
            setGameState(GameState.RESET)
        }
    }

    // private functions
    private fun setGameState(state: GameState) {
        startTime = 0f
        gameState = state
    }

    private fun update(delta: Float) {
        world.step(1 / APP_FPS, 6, 2)
        playerBall.moveBall(delta)
        paddleLeft.movePaddle()
        paddleRight.movePaddle()
        //clear the screen
        Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.update()
    }

    private fun gameOver() {
        val gameOverIntent = Intent(activity, GameOver::class.java)
        gameOverIntent.putExtra("SCORE", score)
        game.dispose()
        activity.startActivity(gameOverIntent)
    }

    private fun resetObjectPositions() {
        playerBall.getBallSprite().setPosition(
                (playerBall.getBallBody().position.x * PPM * SCALE) - playerBall.getBallSprite().width / 2f,
                (playerBall.getBallBody().position.y * PPM * SCALE) - playerBall.getBallSprite().height / 2f)
        playerBall.getBallBody().setTransform((playerBall.getBallSprite().x) / PPM / SCALE,
                playerBall.getBallSprite().y / PPM / SCALE,
                playerBall.getBallBody().angle)
    }

    private fun renderRounds() {
        val roundUITexture1 = AssetManager.roundIndicator
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
            }
        }
    }

    private fun setOnContactListener() {
        // collision detections
        world.setContactListener(object : BallContactListener(this, playerBall,
                paddleLeft, paddleRight, deathZoneLeft, deathZoneRight) {})
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
        val backgroundTexture = AssetManager.backGround
        batch.draw(backgroundTexture, 0f, 0f, screenWidth, screenHeight)
    }

    private fun renderBats() {
        batch.draw(paddleLeft.getPaddleSprite(), paddleLeft.getPaddleSprite().x,
                paddleLeft.getPaddleSprite().y)
        batch.draw(paddleRight.getPaddleSprite(), paddleRight.getPaddleSprite().x,
                paddleRight.getPaddleSprite().y)
    }

    private fun setupPlayArea() {
        paddleLeft = Paddle(this,
                Constants.adjustPaddleStartX,
                camera.viewportHeight / 2, false,
                AssetManager.paddleLeftSound)
        paddleRight = Paddle(this,
                camera.viewportWidth - Constants.adjustPaddleStartX,
                camera.viewportHeight / 2f, true,
                AssetManager.paddleRightSound)
        deathZoneLeft = DeathZone(world, Constants.deathZoneWidth,
                camera.viewportHeight - Constants.adjustDeathZoneStartY,
                0f, camera.viewportHeight / 2f)
        deathZoneRight = DeathZone(world, Constants.deathZoneWidth,
                camera.viewportHeight - Constants.adjustDeathZoneStartY,
                camera.viewportWidth,
                camera.viewportHeight / 2)
        // Joint between paddle and deathZone
        createJoint(deathZoneLeft.getDeathZoneBody(),
                paddleLeft.getPaddleBody(),
                camera.viewportHeight / 2,
                -camera.viewportHeight / 2,
                Vector2(Constants.jointWidth / PPM, 0f),
                Vector2(0f, 0f))
        createJoint(deathZoneRight.getDeathZoneBody(),
                paddleRight.getPaddleBody(),
                camera.viewportHeight / 2,
                -camera.viewportHeight / 2,
                Vector2(-Constants.jointWidth / PPM, 0f),
                Vector2(0f, 0f))
        playerBall = GameObjectBall(this)
    }

    private fun createJoint(bodyA: Body, bodyB: Body, upperLimit: Float, lowerLimit: Float,
                            anchorA: Vector2, anchorB: Vector2): Joint {
        val pDef = PrismaticJointDef()
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



