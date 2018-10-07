package com.example.anni.riggedpongsensorproject.utils

    enum class ObjectBits(val bits: Short) {
        PADDLE(0x01),
        BALL(0x02),
        WALL(0x04),
        DEATH(0x06)
    }