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

fun MatchViewParam.toModel() = MatchScore(
    type = matchType,
    currentGame = currentGame.toModel(),
    games = games.map { it.toModel() }.toMutableList(),
    teamA = teamA.toModel(),
    teamB = teamB.toModel(),
)

fun MatchScore.toViewParam() = MatchViewParam(
    matchType = type,
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

fun GameViewParam.toModel() = Game(
    index = index,
    scoreA = scoreA.toModel(),
    scoreB = scoreB.toModel(),
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
)

fun Player.toVp() = PlayerViewParam(
    name = name,
    onServe = false, // todo
)

fun PlayerViewParam.toModel() = Player(
    name = name,
)

fun Team.toVp() = TeamViewParam(
    player1 = player1.toVp(),
    player2 = player2.toVp(),
    onServe = false, // todo
)

fun TeamViewParam.toModel() = Team(
    player1 = player1.toModel(),
    player2 = player2.toModel()
)