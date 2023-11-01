package com.moengage.dailyhunt.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.moengage.dailyhunt.core.data.model.NewsArticle
import com.moengage.dailyhunt.databinding.ActivityMainBinding
import com.moengage.dailyhunt.ui.recycler.NewsArticleRecyclerAdapter
import com.moengage.dailyhunt.ui.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _viewBinding: ActivityMainBinding? = null
    private val viewBinding: ActivityMainBinding get() = _viewBinding!!

    private lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        viewModel.getNewsArticles().observe(this) { newsArticles ->
            with(viewBinding) {
                with(newsRecycler) {
                    layoutManager =
                        LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                    adapter = NewsArticleRecyclerAdapter(
                        newsArticles,
                        object : NewsArticleRecyclerAdapter.NewsArticleRecyclerListener {
                            override fun onClickRead(position: Int, article: NewsArticle) {
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.data = Uri.parse(article.url)
                                startActivity(intent)
                            }
                        }
                    )
                }
                progressBar.visibility = View.GONE
                newsRecycler.visibility = View.VISIBLE
            }
        }
    }


}