package com.imams.boardminton.domain.di

import com.imams.boardminton.domain.impl.MatchEngineUseCase
import com.imams.boardminton.domain.impl.MatchEngineUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object EngineModule {

    @Provides
    @Singleton
    fun provideMatchEngineUseCase(): MatchEngineUseCase {
        return MatchEngineUseCaseImpl()
    }

}