package com.imams.data.team.repository

import com.imams.data.team.model.Team
import kotlinx.coroutines.flow.Flow

interface TeamRepository {

    suspend fun getAllTeams(): Flow<List<Team>>

    suspend fun getTeam(id: Int): Flow<Team?>

    suspend fun getTeam(first: Int, second: Int): Flow<Team?>

    suspend fun addTeam(team: Team)

    suspend fun addTeams(team: List<Team>)

    suspend fun updateTeam(team: Team)

    suspend fun removeTeam(team: Team)
}