package com.imams.boardminton.domain.di

import com.imams.boardminton.domain.impl.CreatePlayerUseCase
import com.imams.boardminton.domain.impl.CreatePlayerUseCaseImpl
import com.imams.boardminton.domain.impl.CreateTeamUseCase
import com.imams.boardminton.domain.impl.CreateTeamUseCaseImpl
import com.imams.data.player.dao.PlayerDao
import com.imams.data.player.repository.PlayerRepository
import com.imams.data.team.repository.TeamRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object CreatePlayerModule {

    @Provides
    @ViewModelScoped
    fun provideCreatePlayerUseCase(repository: PlayerRepository): CreatePlayerUseCase {
        return CreatePlayerUseCaseImpl(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideCreateTeamUseCase(playerDao: PlayerDao, teamRepo: TeamRepository): CreateTeamUseCase {
        return CreateTeamUseCaseImpl(playerDao, teamRepo)
    }

}