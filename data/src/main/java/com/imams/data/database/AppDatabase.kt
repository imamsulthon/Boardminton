package com.imams.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.imams.data.match.dao.MatchDao
import com.imams.data.match.dao.MatchEntity
import com.imams.data.player.dao.PlayerDao
import com.imams.data.player.dao.PlayerEntity
import com.imams.data.team.dao.TeamDao
import com.imams.data.team.dao.TeamEntity

@Database(
    entities = [PlayerEntity::class, TeamEntity::class, MatchEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    abstract fun teamDao(): TeamDao
    abstract fun matchDao(): MatchDao
}