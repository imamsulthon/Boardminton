package com.imams.boardminton.engine.data.model

data class Score(
    var point: Int = 0
) {

    var onServe: Boolean = false
    var lastPoint: Boolean = false
    var isWin: Boolean = false

    fun add() {
        point++
        onServe = true
    }

    fun min() {
        point--
    }

    fun reset() {
        point = 0
        onServe = false
        lastPoint = false
    }
}

data class Game(
    val index: Int = 0,
    val scoreA: Score = Score(),
    val scoreB: Score = Score(),
    val onServe: OnServe = OnServe.NONE,
    val winner: Winner = Winner.None,
)

data class Player(
    var name: String,
)

data class Team(
    val player1: Player,
    val player2: Player,
) {
    private val _alias: String = player1.name + "/" + player2.name
    val alias = _alias
}

data class MatchScore(
    val id: Int,
    val type: MatchType = MatchType.Single,
    val currentGame: Game,
    val games: MutableList<Game> = mutableListOf(),
    val teamA: Team,
    val teamB: Team,
    var winner: Winner = Winner.None
)

