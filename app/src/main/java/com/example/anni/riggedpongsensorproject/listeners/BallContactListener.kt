package com.example.anni.riggedpongsensorproject.listeners

import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.example.anni.riggedpongsensorproject.screens.GameScreen
import com.example.anni.riggedpongsensorproject.objects.DeathZone
import com.example.anni.riggedpongsensorproject.objects.GameObjectBall
import com.example.anni.riggedpongsensorproject.objects.Paddle
import com.example.anni.riggedpongsensorproject.utils.GameState
import com.example.anni.riggedpongsensorproject.utils.Paddles

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
                gameScreen.setGameState(GameState.GAME_OVER)
            }
        }
        // collision with paddles, increase score here
        if (fixA == paddleLeft.getPaddleBody() || fixA == paddleRight.getPaddleBody()
                && fixB == playerBall.getBallBody()) {
            playerBall.paddleContact = true
            // set the current paddle contact
            when (fixA) {
                paddleLeft.getPaddleBody() -> playerBall.currentPaddle = Paddles.LEFT_PADDLE
                paddleRight.getPaddleBody() -> playerBall.currentPaddle = Paddles.RIGHT_PADDLE
            }
            // if no previous contact with a paddle, set previous contact paddle
            if (playerBall.previousPaddle == Paddles.NO_PADDLE) {
                gameScreen.score += 10
                if (fixA == paddleLeft.getPaddleBody()) playerBall.previousPaddle = Paddles.LEFT_PADDLE
                if (fixA == paddleRight.getPaddleBody()) playerBall.previousPaddle = Paddles.RIGHT_PADDLE
            // increase score only when paddles are contacted in turns
            } else {
                if (playerBall.currentPaddle == Paddles.LEFT_PADDLE && playerBall.previousPaddle == Paddles.RIGHT_PADDLE) {
                    gameScreen.score += 10
                    playerBall.previousPaddle = Paddles.LEFT_PADDLE
                }
                if (playerBall.currentPaddle == Paddles.RIGHT_PADDLE && playerBall.previousPaddle == Paddles.LEFT_PADDLE) {
                    gameScreen.score += 10
                    playerBall.previousPaddle = Paddles.RIGHT_PADDLE
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
