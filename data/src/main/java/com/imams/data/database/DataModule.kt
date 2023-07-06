package com.imams.data.database

import android.app.Application
import androidx.room.Room
import com.imams.data.match.dao.MatchDao
import com.imams.data.player.dao.PlayerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase = Room
        .databaseBuilder(
            application,
            AppDatabase::class.java,
            "boardminton_database"
        )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideRegistrationDao(database: AppDatabase): PlayerDao {
        return database.playerDao()
    }

    @Provides
    @Singleton
    fun providesMatchDao(database: AppDatabase): MatchDao {
        return database.matchDao()
    }

}