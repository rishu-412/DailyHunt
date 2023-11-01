package com.moengage.dailyhunt.core.data.datasource

import com.moengage.dailyhunt.core.data.model.NewsArticle
import com.moengage.dailyhunt.core.data.network.NewsArticleNetworkHelper

/**
 * Contains Implementation of [RemoteNewsArticlesDataSource]
 *
 * @property newsArticleNetworkHelper instance of [NewsArticleNetworkHelper]
 */
class RemoteNewsArticlesDataSourceImpl(
    private val newsArticleNetworkHelper: NewsArticleNetworkHelper
) : RemoteNewsArticlesDataSource {
    override suspend fun getNewsArticles(): List<NewsArticle> {
        return newsArticleNetworkHelper.getNewsArticles()
    }
}