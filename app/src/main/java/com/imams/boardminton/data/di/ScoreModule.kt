package com.imams.boardminton.data.di

import com.imams.boardminton.data.domain.GameUseCase
import com.imams.boardminton.data.domain.MatchType
import com.imams.boardminton.data.domain.UseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ScoreModule {

    @Provides
    @Singleton
    fun provideMatchType(): MatchType = MatchType.SINGLE

    @Provides
    @Singleton
    fun provideGameUseCase(matchType: MatchType = MatchType.SINGLE): UseCase {
        return GameUseCase(matchType)
    }

}