package com.imams.boardminton.engine.domain

import com.imams.boardminton.engine.data.model.MatchType
import com.imams.boardminton.engine.data.model.Winner

data class MatchUIState(
    val match : MatchViewParam = MatchViewParam(
        matchType = MatchType.Single,
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
        games = mutableListOf(),
    )
)

data class MatchViewParam(
    val matchType: MatchType = MatchType.Single,
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
)

data class PlayerViewParam(
    val player: String,
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