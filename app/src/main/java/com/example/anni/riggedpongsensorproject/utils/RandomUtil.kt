package com.example.anni.riggedpongsensorproject.utils

import java.util.*

// get a random number from intRange
fun IntRange.random() = Random().nextInt((endInclusive + 1) - start) + start