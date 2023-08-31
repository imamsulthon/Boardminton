package com.imams.boardminton.domain.di

import android.content.Context
import com.imams.boardminton.ui.screen.create.PhotoUriManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PhotoManagerModule {

    @Provides
    @Singleton
    fun providePhotoUriManager(@ApplicationContext context: Context): PhotoUriManager {
        return PhotoUriManager(context)
    }

}