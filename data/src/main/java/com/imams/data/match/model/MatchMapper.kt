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
        matchDuration = matchDuration,
        shuttleCockUsed = shuttleCockUsed,
    )

    fun Match.toEntity(withId: Boolean = false) = MatchEntity(
        type = type,
        teamA = teamA,
        teamB = teamB,
        currentGame = currentGame,
        games = games,
        winner = winner,
        lastUpdate = lastUpdate,
        matchDuration = matchDuration,
        shuttleCockUsed = shuttleCockUsed,
    ).also {
        if (withId) it.id = id
    }

}