package com.imams.data.team.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("team_entity")
data class TeamEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo("player_id_1") val playerId1: Int,
    @ColumnInfo("player_id_2") val playerId2: Int,
    @ColumnInfo("player_1") val playerName1: String,
    @ColumnInfo("player_2") val playerName2: String,
    @ColumnInfo("type") val type: String,
    @ColumnInfo("rank") val rank: Int,
    @ColumnInfo("play") val play: Int,
    @ColumnInfo("win") val win: Int,
    @ColumnInfo("lose") val lose: Int,
)