package com.imams.data.team.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {

    @Query("SELECT * FROM team_entity")
    fun getAllTeam(): Flow<List<TeamEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTeam(entity: TeamEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTeams(entities: List<TeamEntity>)

    @Query("SELECT * FROM team_entity WHERE id=:id")
    fun getById(id: Int): Flow<TeamEntity>

    @Query("SELECT * FROM team_entity WHERE player_1 LIKE :first AND " +
            "player_2 LIKE :last LIMIT 1")
    suspend fun findByName(first: String, last: String): TeamEntity

    @Query("SELECT * FROM team_entity WHERE (player_id_1 LIKE :first AND player_id_2 LIKE :last) " +
            "OR (player_id_1 LIKE:last AND player_id_2 LIKE:first) LIMIT 1")
    fun findByPlayerId(first: Int, last: Int): Flow<TeamEntity>

    @Delete
    suspend fun delete(team: TeamEntity)
}