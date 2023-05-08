package com.imams.boardminton.engine.domain.model

data class Score(
    var point: Int = 0
) {

    var onServe: Boolean = false

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
    }
}

data class Game(
    val index: Int = 0,
    val scoreA: Score = Score(),
    val scoreB: Score = Score(),
    var onServe: OnServe = OnServe.NONE
)

data class Player(
    var name: String
)

data class Team(
    val player1: Player,
    val player2: Player,
    val game: Game,
) {
    val alias: String = player1.name + "/" + player2
}

data class MatchScore(
    val type: MatchType = MatchType.Single
) {
    private var _games = mutableListOf<Game>()
    val games = _games
}
