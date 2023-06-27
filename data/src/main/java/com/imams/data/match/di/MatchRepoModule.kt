package com.imams.data.match.di

import com.imams.data.match.dao.MatchDao
import com.imams.data.match.repository.MatchRepository
import com.imams.data.match.repository.MatchRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MatchRepoModule {

    @Singleton
    @Provides
    fun providesMatchRepository(matchDao: MatchDao): MatchRepository {
        return MatchRepositoryImpl(matchDao)
    }

}