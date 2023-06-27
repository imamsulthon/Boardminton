package com.imams.data.match.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "match_entity")
data class MatchEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo("match_type") val type: String,
    @ColumnInfo("team_a_1") val teamA: String,
    @ColumnInfo("team_b_1") val teamB: String,
    @ColumnInfo("current_game") val currentGame: String,
    @ColumnInfo("games") val games: String,
    @ColumnInfo("winner") val winner: String,
    @ColumnInfo("last_update") val lastUpdate: String,
)
