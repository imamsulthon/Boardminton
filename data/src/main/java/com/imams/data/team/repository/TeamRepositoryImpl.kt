package com.imams.data.team.repository

import com.imams.data.team.dao.TeamDao
import com.imams.data.team.model.Team
import com.imams.data.team.model.TeamMapper.toEntity
import com.imams.data.team.model.TeamMapper.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TeamRepositoryImpl(
    private val dao: TeamDao,
): TeamRepository {

    override suspend fun getAllTeams(): Flow<List<Team>> {
        return dao.getAllTeam().map { it.map { t -> t.toModel() } }
    }

    override suspend fun getTeam(id: Int): Flow<Team> {
        return dao.getById(id).map { it.toModel() }
    }

    override suspend fun getTeam(first: Int, second: Int): Flow<Team?> {
        return dao.findByPlayerId(first, second).map { it.toModel() }
    }

    override suspend fun addTeam(team: Team) {
        log("addTeam $team")
        dao.addTeam(team.toEntity())
    }

    override suspend fun addTeams(team: List<Team>) {
        dao.addTeams(team.map { it.toEntity() })
    }

    override suspend fun updateTeam(team: Team) {
        dao.addTeam(team.toEntity(true))
    }

    override suspend fun removeTeam(team: Team) {
        dao.delete(team.toEntity(true))
    }

    private fun log(m: String) = println("TeamRepository: $m")

}