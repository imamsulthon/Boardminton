package com.imams.data.player.repository

import com.imams.data.player.dao.PlayerDao
import com.imams.data.player.model.Player
import com.imams.data.player.model.PlayerMapper.toEntity
import com.imams.data.player.model.PlayerMapper.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(
    private val dao: PlayerDao,
): PlayerRepository {

    override suspend fun getAllPlayers(): Flow<List<Player>> {
        return dao.getAllPlayer().map { list ->
            list.map { entity -> entity.toModel() }
        }
    }

    override suspend fun getPlayer(id: Int): Flow<Player> {
        TODO("Not yet implemented")
    }

    override suspend fun addPlayer(player: Player) {
        dao.addPlayer(player.toEntity())
    }

    override suspend fun addPlayer(player: List<Player>) {
        dao.addPlayers(player.map { it.toEntity() })
    }

    override suspend fun removePlayer(player: Player) {
        dao.delete(player.toEntity(true))
    }

}