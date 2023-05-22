package com.imams.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.imams.data.player.dao.PlayerDao
import com.imams.data.player.dao.PlayerEntity

@Database(entities = [PlayerEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun playerDao(): PlayerDao
}