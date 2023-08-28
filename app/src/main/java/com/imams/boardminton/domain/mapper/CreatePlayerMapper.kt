package com.imams.boardminton.domain.mapper

import com.imams.boardminton.ui.screen.create.player.CreatePlayerState
import com.imams.boardminton.ui.screen.create.player.CreateTeamState
import com.imams.data.player.model.Player
import com.imams.data.team.model.Team


object UseCaseMapper {

    fun Player.toState() = CreatePlayerState(
        id = id,
        firstName = firstName, lastName = lastName, handPlay = handPlay,
        gender = gender, height = height, weight = weight,
        dob = dob, photoProfileUri = photoProfileUri,
    )

    fun CreatePlayerState.toModel(withId: Boolean = false) = Player(
        firstName = firstName, lastName = lastName, handPlay = handPlay,
        gender = gender, height = height, weight = weight, dob = dob,
        photoProfileUri = photoProfileUri,
    ).also {
        if (withId) it.id = id
    }

    fun Team.toState() = CreateTeamState(
        id = id,
        playerId1 = playerId1, playerId2 = playerId2, playerName1 = playerName1, playerName2 = playerName2,
        profilePhotoUri1 = profilePhotoUri1, profilePhotoUri2 = profilePhotoUri2
    )

    fun CreateTeamState.toModel(withId: Boolean = false) = Team(
        playerId1 = playerId1, playerId2 = playerId2,
        playerName1 = playerName1, playerName2 = playerName2,
        profilePhotoUri1 = profilePhotoUri1, profilePhotoUri2 = profilePhotoUri2,
    ).also {
        if (withId) it.id = id
    }

}