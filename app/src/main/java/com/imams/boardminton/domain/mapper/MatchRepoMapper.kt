package com.imams.boardminton.domain.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.imams.boardminton.domain.model.GameViewParam
import com.imams.boardminton.domain.model.IMatchType
import com.imams.boardminton.domain.model.MatchViewParam
import com.imams.boardminton.domain.model.TeamViewParam
import com.imams.boardminton.engine.data.model.Winner
import com.imams.data.match.model.Match

object MatchRepoMapper {

    fun Match.toVp(): MatchViewParam = MatchViewParam(
        id = id,
        matchType = type.toType(),
        teamA = teamA.asTeam(),
        teamB = teamB.asTeam(),
        winner = winner.asWinner(),
        currentGame = currentGame.toGame(),
        games = games.toGames(),
        lastUpdate = lastUpdate,
    )

    private fun String.asTeam(): TeamViewParam {
        return Gson().fromJson(this, TeamViewParam::class.java)
    }

    private fun String.toType(): IMatchType = when {
        this.equals("double", true) -> IMatchType.Double
        else -> IMatchType.Double
    }

    private fun String.asWinner(): Winner = when {
        this.equals("a", true) -> Winner.A
        this.equals("b", true) -> Winner.B
        else -> Winner.None
    }

    private fun String.toGame(): GameViewParam {
        return Gson().fromJson(this, GameViewParam::class.java)
    }

    private fun String.toGames(): MutableList<GameViewParam> {
        val listType = object : TypeToken<List<GameViewParam>>() {}.type
        return Gson().fromJson(this, listType)
    }

    fun Any.toJson(): String {
        return Gson().toJson(this)
    }

    fun MatchViewParam.toRepo() = Match(
        id = id,
        type = matchType.name,
        teamA = teamA.toJson(),
        teamB = teamB.toJson(),
        currentGame = currentGame.toJson(),
        games = games.toJson(),
        winner = winner.name,
        lastUpdate = System.currentTimeMillis().toString(),
    )

}