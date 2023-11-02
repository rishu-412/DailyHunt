package com.moengage.dailyhunt.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moengage.dailyhunt.R
import com.moengage.dailyhunt.core.data.model.NewsArticle
import com.moengage.dailyhunt.databinding.LayoutNewsArticleItemBinding
import com.moengage.dailyhunt.utils.getFormattedNewsArticleTime

/**
 * Functional Interface to handle the clicks on News Articls
 *
 */
fun interface OnNewsArticleClickedListener {
    fun onClick(position: Int, article: NewsArticle)
}

/**
 * RecyclerView Adapter for News Articles
 *
 * @property newsArticles [List] of [NewsArticle]
 * @property listener instance of [OnNewsArticleClickedListener]
 */
class NewsArticleRecyclerAdapter(
    private val newsArticles: List<NewsArticle>, private val listener: OnNewsArticleClickedListener
) : RecyclerView.Adapter<NewsArticleRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutNewsArticleItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return newsArticles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(newsArticles[position], listener)
    }

    inner class ViewHolder(private val viewBinding: LayoutNewsArticleItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(article: NewsArticle, listener: OnNewsArticleClickedListener) {
            with(viewBinding) {
                newsTitle.text = article.title
                newsDescription.text = article.description
                newsPublishedAt.text = getFormattedNewsArticleTime(article.publishedAt)
                Glide.with(newsImage)
                    .load(article.imageUrl)
                    .placeholder(R.drawable.placeholder_no_image_found)
                    .into(newsImage)
                newsRead.setOnClickListener {
                    listener.onClick(absoluteAdapterPosition, article)
                }
                root.setOnClickListener {
                    listener.onClick(absoluteAdapterPosition, article)
                }
            }
        }
    }
}