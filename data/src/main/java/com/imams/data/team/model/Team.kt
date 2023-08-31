package com.imams.data.team.model

data class Team(
    var id: Int = 0,
    val playerId1: Int = 0,
    val playerId2: Int = 0,
    val playerName1: String,
    val playerName2: String,
    val profilePhotoUri1: String = "",
    val profilePhotoUri2: String = "",
    val type: String = "double",
    val rank: Int = 0,
    val play: Int = 0,
    val win: Int = 0,
    val lose: Int = 0,
)