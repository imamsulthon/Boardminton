package com.imams.boardminton.domain.impl

import com.imams.boardminton.domain.impl.UseCaseMapper.toState
import com.imams.boardminton.ui.screen.create.player.CreatePlayerState
import com.imams.data.player.model.Player
import com.imams.data.player.repository.PlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class CreatePlayerUseCaseImpl constructor(
    private val repository: PlayerRepository,
): CreatePlayerUseCase {

    override suspend fun createPlayer(data: CreatePlayerState) {
        val player = Player(
            firstName = data.firstName,
            lastName = data.lastName,
            gender = data.gender,
            handPlay = data.handPlay
        )
        repository.addPlayer(player)
    }

    override suspend fun getAllPlayers(): Flow<List<CreatePlayerState>> {
        return repository.getAllPlayers().map { it.map { p -> p.toState() } }.flowOn(Dispatchers.IO)
    }

}

object UseCaseMapper {

    fun Player.toState() = CreatePlayerState(
        id = id,
        firstName = firstName, lastName = lastName, handPlay = handPlay, gender = gender
    )

}