package com.imams.data.match.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {

    @Query("SELECT * FROM match_entity")
    fun getAllMatches(): Flow<List<MatchEntity>>

    @Query("SELECT * FROM match_entity ORDER BY id DESC LIMIT 1")
    fun getLatestMatch(): Flow<MatchEntity?>

    @Query("SELECT * FROM match_entity WHERE winner=:status")
    fun onGoingMatches(status: String): Flow<List<MatchEntity>>

    @Query("SELECT * FROM match_entity WHERE id=:id")
    fun getMatch(id: Int): Flow<MatchEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMatch(match: MatchEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMatch(matchEntity: MatchEntity): Int

    @Transaction
    suspend fun addOrUpdate(match: MatchEntity): Long {
        val id = addMatch(match)
        return if (id == -1L) {
            updateMatch(match)
            match.id.toLong()
        } else {
            id
        }
    }

    @Delete
    suspend fun delete(match: MatchEntity): Int

}