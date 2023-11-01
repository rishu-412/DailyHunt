package com.moengage.dailyhunt.core.data.datasource

import com.moengage.dailyhunt.core.data.model.NewsArticle

/**
 * Declaration for the data source of News Articles
 *
 */
interface RemoteNewsArticlesDataSource {

    suspend fun getNewsArticles(): List<NewsArticle>
}