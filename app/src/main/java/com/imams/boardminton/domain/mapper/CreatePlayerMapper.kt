package com.imams.boardminton.domain.mapper

import com.imams.boardminton.ui.screen.create.player.CreatePlayerState
import com.imams.data.player.model.Player


object UseCaseMapper {

    fun Player.toState() = CreatePlayerState(
        id = id,
        firstName = firstName, lastName = lastName, handPlay = handPlay, gender = gender
    )

    fun CreatePlayerState.toModel(withId: Boolean = false) = Player(
        firstName = firstName, lastName = lastName, handPlay = handPlay, gender = gender
    ).also {
        if (withId) it.id = id
    }

}