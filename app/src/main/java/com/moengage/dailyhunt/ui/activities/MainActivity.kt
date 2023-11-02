package com.moengage.dailyhunt.ui.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.moengage.dailyhunt.core.data.model.NewsArticle
import com.moengage.dailyhunt.databinding.ActivityMainBinding
import com.moengage.dailyhunt.ui.recycler.NewsArticleRecyclerAdapter
import com.moengage.dailyhunt.ui.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

private const val EXTRA_KEY_NOTIFICATION_PAYLOAD = "notification_payload"
private const val ATTRIBUTE_NEWS_URL = "new_url"

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NewsArticleRecyclerAdapter.NewsArticleRecyclerListener {
    private val tag = "MainActivity"
    private var _viewBinding: ActivityMainBinding? = null
    private val viewBinding: ActivityMainBinding get() = _viewBinding!!

    private lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        parseIncomingData(intent)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        viewModel.getNewsArticles().observe(this) { newsArticles ->
            with(viewBinding) {
                with(newsRecycler) {
                    layoutManager =
                        LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                    adapter = NewsArticleRecyclerAdapter(
                        newsArticles, this@MainActivity
                    )
                }
                progressBar.visibility = View.GONE
                newsRecycler.visibility = View.VISIBLE
            }
        }
    }

    override fun onClickRead(position: Int, article: NewsArticle) {
        launchReadArticleIntent(article.url)
    }

    /**
     * Launches an activity to read the detailed article.
     *
     * @param url Link of the News Article
     */
    private fun launchReadArticleIntent(url: String) {
        if (url.isEmpty()) {
            Log.i(
                tag,
                "launchReadArticleIntent(): URL is empty. Cannot redirect to the news article page"
            )
            return
        }
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    /**
     * Parse necessary data from activity intent extras
     */
    private fun parseIncomingData(intent: Intent?) {
        try {
            if (intent == null) return
            if (intent.hasExtra(EXTRA_KEY_NOTIFICATION_PAYLOAD)) {
                Log.i(tag, "parseIncomingData(): Will try to redirect to the news article page")
                val notificationPayload = intent.getStringExtra(EXTRA_KEY_NOTIFICATION_PAYLOAD)
                if (notificationPayload == null) {
                    Log.i(
                        tag,
                        "parseIncomingData(): notification payload is null. can not redirect " + "to news articles page"
                    )
                    return
                }
                Log.d(tag, "parseIncomingData(): $notificationPayload")
                launchReadArticleIntent(JSONObject(notificationPayload).getString(ATTRIBUTE_NEWS_URL))
            }
        } catch (t: Throwable) {
            Log.e(tag, "parseIncomingData(): ", t)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        parseIncomingData(intent)
    }

    companion object {
        fun getNotificationActionIntent(context: Context, payload: String): Intent {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            intent.putExtra(EXTRA_KEY_NOTIFICATION_PAYLOAD, payload)
            return intent
        }
    }

}