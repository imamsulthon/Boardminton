package com.imams.boardminton.domain.di

import android.content.Context
import com.imams.boardminton.ui.settings.DesignPreferenceStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {

    @Singleton
    @Provides
    fun provideAppsSettings(@ApplicationContext context: Context): DesignPreferenceStore {
        return DesignPreferenceStore(context)
    }

}