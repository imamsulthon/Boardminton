package com.imams.boardminton.domain.di

import com.imams.boardminton.domain.impl.CreatePlayerUseCase
import com.imams.boardminton.domain.impl.CreatePlayerUseCaseImpl
import com.imams.data.player.repository.PlayerRepository
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

}