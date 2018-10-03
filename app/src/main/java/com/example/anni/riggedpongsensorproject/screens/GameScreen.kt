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
import com.example.anni.riggedpongsensorproject.sprites.Bat

class GameScreen(mGame: RiggedPong): Screen {

    // batch
    private val batch = mGame.getSpriteBatch()
    // camera
    private val camera = OrthographicCamera()
    // sprites
    private val textureAtlas = TextureAtlas("rp_sprites.atlas")
    // screen size
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels.toFloat()
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels.toFloat()
    // box2d
    private val world = World(Vector2(0f,0f), true) //no gravity
    private val b2Debug = Box2DDebugRenderer() // render body objects for debugging
    // player
     private val playerBall = GameObjectBall(this)
    // objects
   // private val batLeft = Bat(this, 0)
    //private val batRight = Bat(this, 1)

    init {
        camera.setToOrtho(false, screenWidth, screenHeight)
    }

    fun getWorld(): World {
        return world
    }

    fun getAtlasObjects(): TextureAtlas {
        return textureAtlas
    }

    fun createWallBody(world: World, x: Float, y: Float, width: Float, height: Float): Body {
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.StaticBody
        bodyDef.fixedRotation = true
        // 1 to 1 dimensions, meaning 1 in physics engine is 1 pixel
        // Set our body to the same position as our sprite (for ball and bats)
        bodyDef.position.set(x/ RiggedPong.PPM, y/ RiggedPong.PPM)
        // Create a body in the world using our definition
        val b2body = world.createBody(bodyDef)
        // Now define the dimensions of the physics shape
        val wallShape = PolygonShape()
        wallShape.setAsBox(width /2, height/2)
        var fixtureDef = FixtureDef()
        fixtureDef.shape = wallShape
        fixtureDef.density = 1f
        var fixture = b2body.createFixture(fixtureDef)
        // Shape is the only disposable of the lot, so get rid of it
        wallShape.dispose()
        return b2body
    }

    private fun renderBackground() {
        val backgroundTexture = textureAtlas.findRegion("RP_Asset_Play_Area")
        batch.draw(backgroundTexture, 0f, 0f, screenWidth, screenHeight)
    }

    private fun renderBats() {
       // batLeft.draw(batch)
        //batRight.draw(batch)
    }

    private fun update() {
        world.step(1/RiggedPong.FPS, 6, 2)
        updateCamera()
        //batch.projectionMatrix = camera.combined
    }

    private fun updateCamera() {
        val position = camera.position
        position.x = playerBall.getBallBody().position.x * RiggedPong.PPM
        position.y = playerBall.getBallBody().position.y * RiggedPong.PPM
        camera.position.set(position)
        camera.update()
    }

    //called ~60 times per second, game logic updates performed here
    override fun render(delta: Float) {
        update()
        // clear the screen
        Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.begin()
        renderBackground()
       // batch.draw(playerBall.texture, playerBall.getBallBody().position.x * RiggedPong.PPM - (playerBall.texture.width/ 2),
         //       playerBall.getBallBody().position.y * RiggedPong.PPM - (playerBall.texture.width/ 2))
        playerBall.draw(batch)
       // playerBall.moveBall(delta)
        batch.end()
        b2Debug.render(world, camera.combined.scl(RiggedPong.PPM))
    }

    override fun hide() {}

    override fun show() { //TODO: create wall body here!
    }

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
        b2Debug.dispose()
        world.dispose()
        this.dispose()
    }

  /*  private fun createWalls() {
        var body: Body
        var bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.StaticBody
        body = world.createBody(bodyDef)

        val chainShape = ChainShape()
        var vertices = Array<Vector2>()
        vertices.add(Vector2(0f/ RiggedPong.PPM , 0f))
        vertices.add(Vector2(camera.viewportWidth/ RiggedPong.PPM, 0f))
        vertices.add(Vector2(camera.viewportWidth/ RiggedPong.PPM, camera.viewportHeight/ RiggedPong.PPM))
        vertices.add(Vector2(0f/ RiggedPong.PPM, camera.viewportHeight/ RiggedPong.PPM))
        vertices.add(Vector2(0f / RiggedPong.PPM, 0F))
        chainShape.createChain(vertices.items)

        body.createFixture(chainShape, 1f)
        chainShape.dispose()
    }*/
}