package com.imams.data.player.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {

    @Query("SELECT * FROM player_entity")
    fun getAllPlayer(): Flow<List<PlayerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlayer(entity: PlayerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlayers(entities: List<PlayerEntity>)

    @Query("SELECT * FROM player_entity WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    suspend fun findByName(first: String, last: String): PlayerEntity

    @Delete
    suspend fun delete(user: PlayerEntity)

}