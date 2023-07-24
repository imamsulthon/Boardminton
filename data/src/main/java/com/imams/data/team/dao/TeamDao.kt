package com.imams.data.team.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface TeamDao {

    @Query("SELECT * FROM team_entity")
    fun getAllTeam(): Flow<List<TeamEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTeam(entity: TeamEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTeams(entities: List<TeamEntity>)

    @Query("SELECT * FROM player_entity WHERE id=:id")
    fun getById(id: Int): Flow<TeamEntity>

    @Query("SELECT * FROM player_entity WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    suspend fun findByName(first: String, last: String): TeamEntity

    @Delete
    suspend fun delete(team: TeamEntity)
}