package com.moengage.dailyhunt.core.di

import android.content.Context
import com.moengage.dailyhunt.core.data.datasource.RemoteNewsArticlesDataSource
import com.moengage.dailyhunt.core.data.datasource.RemoteNewsArticlesDataSourceImpl
import com.moengage.dailyhunt.core.data.network.NewsArticleNetworkHelper
import com.moengage.dailyhunt.core.data.network.NewsArticleNetworkHelperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesNewsArticleNetworkHelper(@ApplicationContext applicationContext: Context): NewsArticleNetworkHelper {
        return NewsArticleNetworkHelperImpl(applicationContext)
    }

    @Provides
    @Singleton
    fun providesRemoteNewsArticleDataSource(
        newsArticleNetworkHelper: NewsArticleNetworkHelper
    ): RemoteNewsArticlesDataSource {
        return RemoteNewsArticlesDataSourceImpl(newsArticleNetworkHelper)
    }
}