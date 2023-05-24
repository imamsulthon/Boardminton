package com.imams.data.player.model

data class Player(
    var id: Int = 0,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val handPlay: String,
    val height: Int = 0,
    val weight: Int = 0,
)
