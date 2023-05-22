package com.imams.boardminton.domain.impl

import com.imams.boardminton.ui.screen.create.player.CreatePlayerState
import com.imams.data.player.model.Player
import kotlinx.coroutines.flow.Flow

interface CreatePlayerUseCase {

    suspend fun createPlayer(data: CreatePlayerState)

    suspend fun getAllPlayers(): Flow<List<CreatePlayerState>>

}