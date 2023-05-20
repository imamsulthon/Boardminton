package com.imams.boardminton.domain.di

import com.imams.boardminton.domain.impl.MatchBoardUseCase
import com.imams.boardminton.domain.impl.CombinedMatchBoardUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EngineModule {

    @Provides
    @Singleton
    fun provideMatchEngineUseCase(): MatchBoardUseCase {
        return CombinedMatchBoardUseCaseImpl()
    }

}