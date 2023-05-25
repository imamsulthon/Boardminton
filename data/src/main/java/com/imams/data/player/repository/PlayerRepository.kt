package com.imams.data.player.repository

import com.imams.data.player.model.Player
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {

    suspend fun getAllPlayers(): Flow<List<Player>>

    suspend fun getPlayer(id: Int): Flow<Player>

    suspend fun addPlayer(player: Player)

    suspend fun addPlayer(player: List<Player>)

    suspend fun removePlayer(player: Player)

}