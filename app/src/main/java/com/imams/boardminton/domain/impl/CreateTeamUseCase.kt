package com.imams.boardminton.domain.impl

import com.imams.boardminton.domain.model.TeamViewParam
import com.imams.data.player.repository.PlayerRepository
import com.imams.data.team.model.Team
import com.imams.data.team.repository.TeamRepository
import kotlinx.coroutines.flow.first

class CreateTeamUseCaseImpl(
    private val playerRepository: PlayerRepository,
    private val teamRepository: TeamRepository,
): CreateTeamUseCase {

    override suspend fun createTeam(team: Team) {
        val t1 = playerRepository.getPlayer(team.playerName1).first()
        val t2 = playerRepository.getPlayer(team.playerName2).first()
        val checkTeam: Team? = if (t1 == null && t2 == null) null
        else teamRepository.getTeam(t1?.id ?: 0, t2?.id ?: 0).first()
        if (checkTeam != null) return
        val newTeam = Team(
            playerId1 = t1?.id ?: 0,
            playerName1 = team.playerName1,
            profilePhotoUri1 = team.profilePhotoUri1,
            playerId2 = t2?.id ?: 0,
            playerName2 = team.playerName2,
            profilePhotoUri2 = team.profilePhotoUri2,
        )
        teamRepository.addTeam(newTeam)
    }

    override suspend fun createTeam(team: TeamViewParam) {
        val t1 = playerRepository.getPlayer(team.player1.name).first()
        val t2 = playerRepository.getPlayer(team.player2.name).first()
        val checkTeam: Team? = if (t1 == null && t2 == null) null
        else teamRepository.getTeam(t1?.id ?: 0, t2?.id ?: 0).first()
        if (checkTeam != null) return
        val newTeam = Team(
            playerId1 = t1?.id ?: 0,
            playerName1 = team.player1.name,
            playerId2 = t2?.id ?: 0,
            playerName2 = team.player2.name
        )
        teamRepository.addTeam(newTeam)
    }

}

interface CreateTeamUseCase {

    suspend fun createTeam(team: Team)
    suspend fun createTeam(team: TeamViewParam)

}