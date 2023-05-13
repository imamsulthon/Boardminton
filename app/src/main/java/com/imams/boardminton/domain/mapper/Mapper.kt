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

fun MatchViewParam.toVp() = MatchScore(
    type = matchType.toModel(),
    currentGame = currentGame.toVp(),
    games = games.map { it.toVp() }.toMutableList(),
    teamA = teamA.toVp(),
    teamB = teamB.toVp(),
)

fun MatchScore.toViewParam() = MatchViewParam(
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
    winner = winner
)

fun GameViewParam.toVp() = Game(
    index = index,
    scoreA = scoreA.toVp(),
    scoreB = scoreB.toVp(),
    winner = winner
)


fun Score.toVp() = ScoreViewParam(
    point = point,
    onServe = onServe,
    isLastPoint = lastPoint,
    isWin = isWin,
)

fun ScoreViewParam.toVp() = Score(
    point = point,
)

fun Player.toVp() = PlayerViewParam(
    name = name,
    onServe = false, // todo
)

fun PlayerViewParam.toVp() = Player(
    name = name,
)

fun Team.toVp() = TeamViewParam(
    player1 = player1.toVp(),
    player2 = player2.toVp(),
    onServe = false, // todo
)

fun TeamViewParam.toVp() = Team(
    player1 = player1.toVp(),
    player2 = player2.toVp()
)