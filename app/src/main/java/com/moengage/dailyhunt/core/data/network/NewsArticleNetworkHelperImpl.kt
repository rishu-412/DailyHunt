package com.moengage.dailyhunt.core.data.network

import android.content.Context
import android.util.Log
import com.moengage.dailyhunt.core.data.model.NetworkResponse
import com.moengage.dailyhunt.core.data.model.NewsArticle
import com.moengage.dailyhunt.core.data.model.NewsSource
import com.moengage.dailyhunt.utils.hasInternetConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

private const val NEWS_ARTICLE_URL =
    "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json"
private const val REQUEST_METHOD = "GET"

private const val BUFFER_SIZE = 1024

//ROOT PAYLOAD
private const val ATTRIBUTE_ARTICLES = "articles"
private const val ATTRIBUTE_STATUS = "status"

//NEWS ARTICLE PAYLOAD
private const val ATTRIBUTE_AUTHOR = "author"
private const val ATTRIBUTE_TITLE = "title"
private const val ATTRIBUTE_DESCRIPTION = "description"
private const val ATTRIBUTE_URL = "url"
private const val ATTRIBUTE_IMAGE_URL = "urlToImage"
private const val ATTRIBUTE_PUBLISHED_AT = "publishedAt"
private const val ATTRIBUTE_CONTENT = "content"
private const val ATTRIBUTE_SOURCE = "source"
private const val ATTRIBUTE_SOURCE_ID = "id"
private const val ATTRIBUTE_SOURCE_NAME = "name"

/**
 *  Contains the implementation News Article Network Helper
 *
 * @property applicationContext instance of [Context]
 */
class NewsArticleNetworkHelperImpl(private val applicationContext: Context) :
    NewsArticleNetworkHelper {

    private val tag = "NetworkHelper"

    override suspend fun getNewsArticles(): List<NewsArticle> {
        return withContext(Dispatchers.IO) {
            requestNewsArticles()
        }
    }

    /**
     * Makes a network call to fetch and provide the list of [NewsArticle]
     *
     * @return [List] of [NewsArticle]
     */
    private fun requestNewsArticles(): List<NewsArticle> {
        Log.i(tag, "requestNewsArticles(): will try to fetch the news articles")

        //First check for the internet connectivity to make a network call
        if (!hasInternetConnection(applicationContext)) {
            Log.i(
                tag, "requestNewsArticles(): no internet connectivity. Please check your " +
                        "internet before proceeding further."
            )
            return emptyList()
        }

        //Establish Network
        val url = URL(NEWS_ARTICLE_URL)
        val httpConnection = url.openConnection() as HttpURLConnection
        httpConnection.requestMethod = REQUEST_METHOD
        httpConnection.connect()

        //Parse the network response and return the data fetched from the network call
        return parseNetworkResponse(parseInputStreamToJson(httpConnection.inputStream)).data
    }

    /**
     * Parses [InputStream] to [JSONObject]
     *
     * @param inputStream instance of [InputStream]
     * @return [JSONObject] containing the response payload
     */
    private fun parseInputStreamToJson(inputStream: InputStream): JSONObject {
        val outputStream = ByteArrayOutputStream()
        return try {
            val buffer = ByteArray(BUFFER_SIZE)
            var length: Int
            while (-1 != inputStream.read(buffer).also { length = it }) {
                outputStream.write(buffer, 0, length)
            }
            JSONObject(String(outputStream.toByteArray()))
        } catch (t: Throwable) {
            Log.e(tag, "getNewsArticles: ", t)
            JSONObject()
        } finally {
            inputStream.close()
            outputStream.close()
        }
    }

    /**
     * Parses [NetworkResponse] from the http response payload
     *
     * @param payload instance oj [JSONObject]
     * @return [NetworkResponse]
     */
    private fun parseNetworkResponse(payload: JSONObject): NetworkResponse<List<NewsArticle>> {
        return NetworkResponse(
            payload.getString(ATTRIBUTE_STATUS),
            parseNewsArticles(payload.getJSONArray(ATTRIBUTE_ARTICLES))
        )
    }

    /**
     * Parses list of [NewsArticle] from json payload
     *
     * @param listOfArticlePayload instance of [JSONObject]
     * @return [List] of [NewsArticle]
     */
    private fun parseNewsArticles(listOfArticlePayload: JSONArray): List<NewsArticle> {
        val list = mutableListOf<NewsArticle>()
        for (i in 0 until listOfArticlePayload.length()) {
            list.add(parseNewsArticle(listOfArticlePayload.getJSONObject(i)))
        }
        return list
    }

    /**
     * Parses [NewsArticle] from json payload
     *
     * @param newsArticlePayload instance of [JSONObject]
     * @return [NewsArticle]
     */
    private fun parseNewsArticle(newsArticlePayload: JSONObject): NewsArticle {
        return NewsArticle(
            authorName = newsArticlePayload.getString(ATTRIBUTE_AUTHOR),
            title = newsArticlePayload.getString(ATTRIBUTE_TITLE),
            description = newsArticlePayload.getString(ATTRIBUTE_DESCRIPTION),
            url = newsArticlePayload.getString(ATTRIBUTE_URL),
            imageUrl = newsArticlePayload.getString(ATTRIBUTE_IMAGE_URL),
            publishedAt = newsArticlePayload.getString(ATTRIBUTE_PUBLISHED_AT),
            content = newsArticlePayload.getString(ATTRIBUTE_CONTENT),
            newsSource = parseSource(newsArticlePayload.getJSONObject(ATTRIBUTE_SOURCE))
        )
    }

    /**
     * Parse [NewsSource] from json payload
     *
     * @param sourcePayload instance of [JSONObject]
     * @return [NewsSource]
     */
    private fun parseSource(sourcePayload: JSONObject): NewsSource {
        return NewsSource(
            id = sourcePayload.getString(ATTRIBUTE_SOURCE_ID),
            name = sourcePayload.getString(ATTRIBUTE_SOURCE_NAME)
        )
    }

}