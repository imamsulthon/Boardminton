package com.imams.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.imams.data.match.dao.MatchDao
import com.imams.data.match.dao.MatchEntity
import com.imams.data.player.dao.PlayerDao
import com.imams.data.player.dao.PlayerEntity

@Database(
    entities = [PlayerEntity::class, MatchEntity::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    abstract fun matchDao(): MatchDao
}