package com.imams.boardminton.domain.mapper

import com.imams.boardminton.domain.model.GameViewParam
import com.imams.boardminton.domain.model.MatchViewParam
import com.imams.boardminton.domain.model.PlayerViewParam
import com.imams.boardminton.domain.model.ScoreViewParam
import com.imams.boardminton.domain.model.TeamViewParam
import com.imams.boardminton.engine.data.model.Game
import com.imams.boardminton.engine.data.model.MatchScore
import com.imams.boardminton.engine.data.model.Player
import com.imams.boardminton.engine.data.model.Score
import com.imams.boardminton.engine.data.model.Team
import com.imams.boardminton.engine.data.model.Winner
import com.imams.boardminton.ui.utils.getLabel

fun MatchViewParam.toModel() = MatchScore(
    id = id,
    type = matchType.toModel(),
    currentGame = currentGame.toModel(),
    games = games.map { it.toModel() }.toMutableList(),
    teamA = teamA.toModel(),
    teamB = teamB.toModel(),
    winner = this.winner,
)

fun MatchScore.toVp() = MatchViewParam(
    id = id,
    matchType = type.toVp(),
    currentGame = currentGame.toVp(),
    games = games.map { it.toVp() }.toMutableList(),
    teamA = teamA.toVp(),
    teamB = teamB.toVp(),
    winner = winner
)

fun Game.toVp() = GameViewParam(
    index = index,
    scoreA = scoreA.toVp(),
    scoreB = scoreB.toVp(),
    onServe = onServe,
    winner = winner
)

fun GameViewParam.toModel() = Game(
    index = index,
    scoreA = scoreA.toModel(),
    scoreB = scoreB.toModel(),
    onServe = onServe,
    winner = winner
)


fun Score.toVp() = ScoreViewParam(
    point = point,
    onServe = onServe,
    isLastPoint = lastPoint,
    isWin = isWin,
)

fun ScoreViewParam.toModel() = Score(
    point = point,
).also {
    it.onServe = onServe
    it.lastPoint = isLastPoint
    it.isWin = isWin
}

fun Player.toVp() = PlayerViewParam(
    name = name,
    onServe = onServe,
)

fun PlayerViewParam.toModel() = Player(
    name = name,
    onServe = onServe,
)

fun Team.toVp() = TeamViewParam(
    player1 = player1.toVp(),
    player2 = player2.toVp(),
    onServe = player1.onServe || player2.onServe
)

fun TeamViewParam.toModel() = Team(
    player1 = player1.toModel(),
    player2 = player2.toModel()
)

fun MatchViewParam.gameWinnerBy(): String {
    return if (currentGame.winner == Winner.A) teamA.getLabel()
    else teamB.getLabel()
}

fun MatchViewParam.matchWinnerBy(): String {
    return if (winner == Winner.A) teamA.getLabel()
    else teamB.getLabel()
}