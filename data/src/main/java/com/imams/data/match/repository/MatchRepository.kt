package com.imams.data.match.repository

import com.imams.data.match.model.Match
import kotlinx.coroutines.flow.Flow

interface MatchRepository {

    suspend fun getAllMatches(): Flow<List<Match>>

    suspend fun getLatestMatch(): Flow<Match?>

    suspend fun getMatch(id: Int): Flow<Match>

    suspend fun saveMatch(match: Match)

    suspend fun updateMatch(match: Match)

    suspend fun deleteMatch(match: Match)
}