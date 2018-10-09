package com.example.anni.riggedpongsensorproject.listeners

import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.example.anni.riggedpongsensorproject.screens.GameScreen
import com.example.anni.riggedpongsensorproject.sprites.DeathZone
import com.example.anni.riggedpongsensorproject.sprites.GameObjectBall
import com.example.anni.riggedpongsensorproject.sprites.Paddle
import com.example.anni.riggedpongsensorproject.utils.PaddleHit

open class BallContactListener(private val gameScreen: GameScreen, private val playerBall: GameObjectBall,
                               private val paddleLeft: Paddle, private val paddleRight: Paddle,
                               private val deathZoneLeft: DeathZone, private val deathZoneRight: DeathZone): ContactListener {

    override fun beginContact(contact: Contact?) {
        val fixA = contact!!.fixtureA.body
        val fixB = contact.fixtureB.body

        // collision with deathzones, decrease rounds by one and reset arena
        if (fixA == deathZoneLeft.getDeathZoneBody() || fixA == deathZoneRight.getDeathZoneBody()
                && fixB == playerBall.getBallBody()) {
            --gameScreen.rounds
            gameScreen.resetPlayArea()
            if (gameScreen.rounds <= 0) {
                //setGameState(GameState.GAME_OVER)
            }
        }
        // collision with paddles, increase score here
        if (fixA == paddleLeft.getPaddleBody() || fixA == paddleRight.getPaddleBody()
                && fixB == playerBall.getBallBody()) {
            playerBall.paddleContact = true
            // set the current paddle contact
            when (fixA) {
                paddleLeft.getPaddleBody() -> playerBall.currentPaddle = PaddleHit.LEFT_PADDLE
                paddleRight.getPaddleBody() -> playerBall.currentPaddle = PaddleHit.RIGHT_PADDLE
            }
            // if no previous contact with a paddle, set previous contact paddle
            if (playerBall.previousPaddle == PaddleHit.NO_PADDLE) {
                gameScreen.score += 10
                if (fixA == paddleLeft.getPaddleBody()) playerBall.previousPaddle = PaddleHit.LEFT_PADDLE
                if (fixA == paddleRight.getPaddleBody()) playerBall.previousPaddle = PaddleHit.RIGHT_PADDLE
            // increase score only when paddles are contacted in turns
            } else {
                if (playerBall.currentPaddle == PaddleHit.LEFT_PADDLE && playerBall.previousPaddle == PaddleHit.RIGHT_PADDLE) {
                    gameScreen.score += 10
                    playerBall.previousPaddle = PaddleHit.LEFT_PADDLE
                }
                if (playerBall.currentPaddle == PaddleHit.RIGHT_PADDLE && playerBall.previousPaddle == PaddleHit.LEFT_PADDLE) {
                    gameScreen.score += 10
                    playerBall.previousPaddle = PaddleHit.RIGHT_PADDLE
                }
            }
        }
    }

    override fun endContact(contact: Contact?) {
        playerBall.paddleContact = false
    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {}
    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {}
}
