package com.imams.boardminton.domain.impl

import com.imams.boardminton.domain.mapper.UseCaseMapper.toModel
import com.imams.boardminton.domain.mapper.UseCaseMapper.toState
import com.imams.boardminton.ui.screen.create.player.CreatePlayerState
import com.imams.data.player.repository.PlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class CreatePlayerUseCaseImpl constructor(
    private val repository: PlayerRepository,
): CreatePlayerUseCase {

    override suspend fun createPlayer(data: CreatePlayerState) {
        repository.addPlayer(data.toModel())
    }

    override suspend fun updatePlayer(data: CreatePlayerState) {
        repository.updatePlayer(data.toModel(true))
    }

    override suspend fun getPlayer(id: Int): Flow<CreatePlayerState> {
        return repository.getPlayer(id).map { it.toState() }
    }

    override suspend fun getAllPlayers(): Flow<List<CreatePlayerState>> {
        return repository.getAllPlayers().map { it.map { p -> p.toState() } }.flowOn(Dispatchers.IO)
    }

    override suspend fun savePlayers(players: List<CreatePlayerState>) {
        repository.addPlayer(players.map { it.toModel(false) })
    }

}

interface CreatePlayerUseCase {
    suspend fun createPlayer(data: CreatePlayerState)
    suspend fun updatePlayer(data: CreatePlayerState)
    suspend fun getPlayer(id: Int): Flow<CreatePlayerState>
    suspend fun getAllPlayers(): Flow<List<CreatePlayerState>>
    suspend fun savePlayers(players: List<CreatePlayerState>)
}