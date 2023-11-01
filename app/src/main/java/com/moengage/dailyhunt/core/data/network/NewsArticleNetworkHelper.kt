package com.moengage.dailyhunt.core.data.network

import com.moengage.dailyhunt.core.data.model.NewsArticle

/**
 * Declaration for News Article Network Helper
 */
interface NewsArticleNetworkHelper {

    suspend fun getNewsArticles(): List<NewsArticle>
}