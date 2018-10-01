package com.example.anni.riggedpongsensorproject.Sprites

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.example.anni.riggedpongsensorproject.GameScreen

class Bat(mGameScreen: GameScreen, mBatch: SpriteBatch, mWorld: World) : Sprite() {

    private val gameScreen = mGameScreen
    private val batch = mBatch
    private val world = mWorld
    private val batTextureRegion = gameScreen.getAtlasObjects().findRegion("RP_Asset_Bat_LEFT")
    //we need a sprite since it's going to move
    private val sprite = Sprite(batTextureRegion)
    private val batTextureHeight = batTextureRegion.originalHeight.toFloat()
    private val batTextureWidth = batTextureRegion.originalWidth.toFloat()

    init {
        // To center it, you need to subtract half of the textures width from the x,
        // and half of the textures height from the y coordinate. Something along these lines:
        //val xBallPosition: Float = (Gdx.graphics.width/ 2f - batTextureWidth/ 2f)
        // position to top and center of the screen
        //setBounds(830f, 898f, batTextureWidth, batTextureHeight)
        setRegion(batTextureRegion)
        createBox2DBat()
    }

    fun setPosition(side: String) {
        val yBatPosition: Float = (Gdx.graphics.height / 2f - batTextureHeight / 2f)
        if (side == "left") {
            setBounds(200f, yBatPosition, batTextureWidth, batTextureHeight)
        } else {
            setBounds(1525f, yBatPosition, batTextureWidth, batTextureHeight)
            setFlip(true, true)
        }
    }

    private fun createBox2DBat() {
        // Create a physics world, the heart of the simulation.  The Vector
        //passed in is gravity
        val world = World(Vector2(0f, -98f), true)
        var body: Body

        // Center the sprite in the top/middle of the screen
        sprite.setPosition(Gdx.graphics.width / 2 - sprite.width / 2, Gdx.graphics.height / 2f)

        // Now create a BodyDefinition.  This defines the physics objects type
        //and position in the simulation
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
        // We are going to use 1 to 1 dimensions.  Meaning 1 in physics engine
        //is 1 pixel
        // Set our body to the same position as our sprite
        bodyDef.position.set(sprite.x, sprite.y)
        // Create a body in the world using our definition
        body = world.createBody(bodyDef)

        // Now define the dimensions of the physics shape
        val shape = PolygonShape()
        // We are a box, so this makes sense, no?
        // Basically set the physics polygon to a box with the same dimensions
        //as our sprite
        shape.setAsBox(sprite.width / 2, sprite.height / 2)

        // FixtureDef is a confusing expression for physical properties
        // Basically this is where you, in addition to defining the shape of the body
        // you also define it's properties like density, restitution and others we will see shortly
        // If you are wondering, density and area are used to calculate over all mass
        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.density = 1f

        val fixture = body.createFixture(fixtureDef)

        // Shape is the only disposable of the lot, so get rid of it
        shape.dispose()
    }
}