package com.imams.boardminton.domain.model

import com.imams.boardminton.engine.data.model.OnServe
import com.imams.boardminton.engine.data.model.Winner

data class MatchUIState(
    val match: MatchViewParam = MatchViewParam(
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
    val id: Int = 0,
) {
    var courtSide: CourtSide = CourtSide()
    var scoreByCourt: ScoreByCourt = getByCourt(courtSide)

    fun setScoreByCourt(courtSide: CourtSide) {
        this.courtSide = courtSide
        scoreByCourt = getByCourt(courtSide)
    }

    private fun getByCourt(courtSide: CourtSide): ScoreByCourt {
        val left = if (courtSide.left == ISide.A) match.currentGame.scoreA
        else match.currentGame.scoreB
        val right = if (courtSide.right == ISide.A) match.currentGame.scoreA
        else match.currentGame.scoreB
        val teamOnLeft = if (courtSide.left == ISide.A) match.teamA
        else match.teamB
        val teamOnRight = if (courtSide.right == ISide.A) match.teamA
        else match.teamB
        return ScoreByCourt(
            index = match.currentGame.index,
            left = left,
            right = right,
            teamLeft = teamOnLeft,
            teamRight = teamOnRight,
        )
    }

}

data class MatchViewParam(
    val matchType: IMatchType = IMatchType.Single,
    val currentGame: GameViewParam = GameViewParam(),
    val teamA: TeamViewParam,
    val teamB: TeamViewParam,
    val games: MutableList<GameViewParam> = mutableListOf(),
    val winner: Winner = Winner.None
)

// region UI State Model
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
    val onServe: OnServe = OnServe.NONE,
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
// endregion
}

data class WinnerState(
    val type: Type,
    val index: Int,
    val show: Boolean = true,
    val isWin: Boolean = false,
    val by: String,
) {
    enum class Type {
        Game, Match
    }
}