package com.imams.data.player.di

import com.imams.data.player.dao.PlayerDao
import com.imams.data.player.repository.PlayerRepository
import com.imams.data.player.repository.PlayerRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlayerModule {

    @Singleton
    @Provides
    fun providesPlayerRepository(playerDao: PlayerDao): PlayerRepository {
        return PlayerRepositoryImpl(playerDao)
    }

}