package com.imams.boardminton.domain.impl

import com.imams.data.player.dao.PlayerDao
import com.imams.data.team.model.Team
import com.imams.data.team.repository.TeamRepository
import kotlinx.coroutines.flow.first

class CreateTeamUseCaseImpl(
    private val playerDao: PlayerDao,
    private val teamRepository: TeamRepository,
): CreateTeamUseCase {

    override suspend fun createTeam(team: Team) {
        val t1 = playerDao.getByName(team.playerName1).first()
        val t2 = playerDao.getByName(team.playerName2).first()
        val newTeam = Team(
            playerId1 = t1.id,
            playerName1 = team.playerName1,
            playerId2 = t2.id,
            playerName2 = team.playerName2
        )
        teamRepository.addTeam(newTeam)
    }
}

interface CreateTeamUseCase {

    suspend fun createTeam(team: Team)

}