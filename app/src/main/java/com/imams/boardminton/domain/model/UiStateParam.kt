package com.imams.boardminton.domain.model

import com.imams.boardminton.engine.data.model.Winner

data class MatchUIState(
    val match : MatchViewParam = MatchViewParam(
        matchType = IMatchType.Single,
        teamA = TeamViewParam(
            player1 = PlayerViewParam(""),
            player2 = PlayerViewParam(""),
            onServe = false
        ),
        teamB = TeamViewParam(
            player1 = PlayerViewParam(""),
            player2 = PlayerViewParam(""),
            onServe = false
        ),
        currentGame = GameViewParam(),
        games = mutableListOf(),
    ),
) {
    var scoreByCourt: ScoreByCourt = ScoreByCourt(
        index = match.currentGame.index,
        left = match.currentGame.scoreA,
        right = match.currentGame.scoreB,
        teamLeft = match.teamA,
        teamRight = match.teamB
    )
}

data class MatchViewParam(
    val matchType: IMatchType = IMatchType.Single,
    val currentGame: GameViewParam = GameViewParam(),
    val teamA: TeamViewParam,
    val teamB: TeamViewParam,
    val games: MutableList<GameViewParam> = mutableListOf(),
    var winner: Winner = Winner.None
)

// UI State Model
data class TeamViewParam(
    val player1: PlayerViewParam,
    val player2: PlayerViewParam,
    val onServe: Boolean,
) {
    val isSingle = player2.name.isEmpty()
}

data class PlayerViewParam(
    val name: String,
    val onServe: Boolean = false,
)

data class GameViewParam(
    val index: Int = 1,
    val scoreA: ScoreViewParam = ScoreViewParam(),
    val scoreB: ScoreViewParam = ScoreViewParam(),
    val winner: Winner = Winner.None
)

data class ScoreViewParam(
    val point: Int = 0,
    val onServe: Boolean = false,
    val isLastPoint: Boolean = false,
    val isWin: Boolean = false,
)

data class ScoreByCourt(
    val index: Int,
    val left: ScoreViewParam = ScoreViewParam(),
    val right: ScoreViewParam = ScoreViewParam(),
    val teamLeft: TeamViewParam,
    val teamRight: TeamViewParam,
)

data class CourtSide(
    var left: ISide = ISide.A,
    var right: ISide = ISide.B,
) {
    fun swap() {
        left = right.also { right = left }
    }
}