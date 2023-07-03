package com.imams.data.match.repository

import com.imams.data.match.dao.MatchDao
import com.imams.data.match.model.Match
import com.imams.data.match.model.MatchMapper.toEntity
import com.imams.data.match.model.MatchMapper.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MatchRepositoryImpl @Inject constructor(
    private val dao: MatchDao,
): MatchRepository {

    private fun log(msg: String) = println("MatchRepository: $msg")

    override suspend fun getLatestMatch(): Flow<Match?> {
        return dao.getLatestMatch().map { it?.toModel() }
    }

    override suspend fun getOnGoingMatches(): Flow<List<Match>?> {
        return dao.onGoingMatches("None").map { list ->
            list.map { it.toModel() }
        }
    }

    override suspend fun getMatch(id: Int): Flow<Match> {
        return dao.getMatch(id).map { it.toModel() }
    }

    override suspend fun getAllMatches(): Flow<List<Match>> {
        return dao.getAllMatches()
            .map { list ->
                list.map { entity ->
                    entity.toModel()
                }
        }
    }

    override suspend fun saveMatch(match: Match) {
        dao.addMatch(match.toEntity())
    }

    override suspend fun updateMatch(match: Match) {
        dao.updateMatch(match.toEntity(true))
    }

    override suspend fun deleteMatch(match: Match) {
        dao.delete(match.toEntity(true))
    }

}