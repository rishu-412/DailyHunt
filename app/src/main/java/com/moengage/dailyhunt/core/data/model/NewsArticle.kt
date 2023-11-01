package com.moengage.dailyhunt.core.data.model

/**
 * News Article Model.
 *
 * @property authorName Author Name [String]
 * @property title News Title [String]
 * @property description News Description [String]
 * @property url News URL [String]
 * @property imageUrl News Image URL [String]
 * @property publishedAt News Published Date [String]
 * @property content News Article Content [String]
 * @property newsSource instance of [NewsSource]
 */
data class NewsArticle(
    val authorName: String,
    val title: String,
    val description: String,
    val url: String,
    val imageUrl: String,
    val publishedAt: String,
    val content: String,
    val newsSource: NewsSource
)