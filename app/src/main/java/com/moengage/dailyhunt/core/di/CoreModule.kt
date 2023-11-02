package com.moengage.dailyhunt.core.di

import android.content.Context
import com.moengage.dailyhunt.core.data.repository.PrefRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun providesPrefRepository(@ApplicationContext applicationContext: Context): PrefRepository {
        return PrefRepository(applicationContext)
    }
}