package com.imams.data.match.model

import com.imams.data.match.dao.MatchEntity

object MatchMapper {

    fun MatchEntity.toModel() = Match(
        id = id,
        type = type,
        teamA = teamA,
        teamB = teamB,
        currentGame = currentGame,
        games = games,
        winner = winner,
        lastUpdate = lastUpdate,
    )

    fun Match.toEntity(withId: Int? = null) = MatchEntity(
        id = id,
        type = type,
        teamA = teamA,
        teamB = teamB,
        currentGame = currentGame,
        games = games,
        winner = winner,
        lastUpdate = lastUpdate,
    ).also {
        if (withId != null) it.id = id
    }

}