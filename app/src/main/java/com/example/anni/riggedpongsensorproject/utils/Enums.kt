package com.example.anni.riggedpongsensorproject.utils

    // for collision filtering
    enum class ObjectBits(val bits: Short) {
        PADDLE(0x01),
        BALL(0x02),
        DEATH(0x06)
    }

    // for keeping up with the game state
    enum class GameState {
        COUNTDOWN,
        PLAY,
        GAME_OVER,
        RESET
    }

    enum class Paddles {
        RIGHT_PADDLE,
        LEFT_PADDLE,
        NO_PADDLE
    }