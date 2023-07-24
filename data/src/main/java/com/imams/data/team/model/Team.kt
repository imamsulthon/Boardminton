package com.imams.data.team.model

data class Team(
    var id: Int = 0,
    val playerId1: String,
    val playerId2: String,
    val playerName1: String,
    val playerName2: String,
    val type: String,
    val rank: Int,
    val play: Int,
    val win: Int,
    val lose: Int,
)