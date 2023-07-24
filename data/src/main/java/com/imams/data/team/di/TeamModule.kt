package com.imams.data.team.di

import com.imams.data.team.dao.TeamDao
import com.imams.data.team.repository.TeamRepository
import com.imams.data.team.repository.TeamRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TeamModule {

    @Provides
    @Singleton
    fun provideTeamRepository(teamDao: TeamDao): TeamRepository {
        return TeamRepositoryImpl(teamDao)
    }

}