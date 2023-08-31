package com.imams.boardminton.domain.impl

import com.imams.boardminton.domain.model.TeamViewParam
import com.imams.data.player.repository.PlayerRepository
import com.imams.data.team.model.Team
import com.imams.data.team.repository.TeamRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class CreateTeamUseCaseImpl(
    private val playerRepository: PlayerRepository,
    private val teamRepository: TeamRepository,
): CreateTeamUseCase {

    override suspend fun createTeam(team: Team) {
        withContext(Dispatchers.IO) {
            val t1 = playerRepository.getPlayer(team.playerName1).first()
            val t2 = playerRepository.getPlayer(team.playerName2).first()
            val checkTeam: Team? = if (t1 == null && t2 == null) null
            else teamRepository.getTeam(t1?.id ?: 0, t2?.id ?: 0).first()
            if (checkTeam != null) return@withContext
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
    }

    override suspend fun createTeam(team: TeamViewParam) {
        try {
            withContext(Dispatchers.IO) {
                val p1 = async { playerRepository.getPlayer(team.player1.name).first() }.await()
                val p2 = async { playerRepository.getPlayer(team.player2.name).first() }.await()
                val checkTeam: Team? = if (p1 == null && p2 == null) null
                else teamRepository.getTeam(p1?.id ?: 0, p2?.id ?: 0).first()
                if (checkTeam != null) return@withContext
                val newTeam = Team(
                    playerId1 = p1?.id ?: 0,
                    playerName1 = team.player1.name,
                    playerId2 = p2?.id ?: 0,
                    playerName2 = team.player2.name
                )
                teamRepository.addTeam(newTeam)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

interface CreateTeamUseCase {

    suspend fun createTeam(team: Team)
    suspend fun createTeam(team: TeamViewParam)

}