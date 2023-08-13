package com.imams.data.player.model

import com.imams.data.player.dao.PlayerEntity

object PlayerMapper {

    fun PlayerEntity.toModel() = Player(
        id = id,
        firstName = firstName,
        lastName = lastName,
        gender = gender,
        weight = weight,
        height = height,
        handPlay = handPlay,
        dob = dob,
    )

    fun Player.toEntity(withId: Boolean = false) = PlayerEntity(
        firstName = firstName,
        lastName = lastName,
        fullName = "$firstName $lastName",
        gender = gender,
        weight = weight,
        height = height,
        handPlay = handPlay,
        dob = dob,
    ).also {
        if (withId) it.id = id
    }

}