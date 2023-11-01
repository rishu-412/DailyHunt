package com.moengage.dailyhunt.core.data.repository

import com.moengage.dailyhunt.core.data.datasource.RemoteNewsArticlesDataSource
import com.moengage.dailyhunt.core.data.model.NewsArticle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(private val remoteNewsArticlesDataSource: RemoteNewsArticlesDataSource) {

    suspend fun getNewsArticles(): List<NewsArticle> {
        return remoteNewsArticlesDataSource.getNewsArticles()
    }
}