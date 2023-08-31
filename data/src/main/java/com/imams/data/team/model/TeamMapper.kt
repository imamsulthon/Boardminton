package com.imams.data.team.model

import com.imams.data.team.dao.TeamEntity

object TeamMapper {

    fun TeamEntity.toModel() = Team(
        id = id,
        playerId1 = playerId1,
        playerId2 = playerId2,
        playerName1 = playerName1,
        playerName2 = playerName2,
        profilePhotoUri1 = profilePhotoUri1,
        profilePhotoUri2 = profilePhotoUri2,
        type = type,
        rank = rank,
        play = play,
        win = win,
        lose = lose,
    )

    fun Team.toEntity(withId: Boolean = false) = TeamEntity(
        playerId1 = playerId1,
        playerId2 = playerId2,
        playerName1 = playerName1,
        playerName2 = playerName2,
        profilePhotoUri1 = profilePhotoUri1,
        profilePhotoUri2 = profilePhotoUri2,
        type = type,
        rank = rank,
        play = play,
        win = win,
        lose = lose,
    ).also {
        if (withId) it.id = id
    }
}