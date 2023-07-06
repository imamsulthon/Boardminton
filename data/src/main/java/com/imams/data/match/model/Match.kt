package com.imams.data.match.model

data class Match(
    var id: Int = 0,
    val type: String,
    val teamA: String,
    val teamB: String,
    val currentGame: String,
    val games: String,
    var winner: String,
    var lastUpdate: String,
    var matchDuration: Long,
)