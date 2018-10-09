package com.example.anni.riggedpongsensorproject.utils

import java.util.*

fun IntRange.random() = Random().nextInt((endInclusive + 1) - start) + start