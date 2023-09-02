package com.imams.data.player.model

data class Player(
    var id: Int = 0,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val handPlay: String,
    val phoneNumber: String = "",
    val height: Int = 0,
    val weight: Int = 0,
    val dob: Long = 0,
    val nationalityCode: String = "",
    val photoProfileUri: String,
)
