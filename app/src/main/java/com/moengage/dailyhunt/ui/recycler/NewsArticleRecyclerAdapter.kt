package com.moengage.dailyhunt.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moengage.dailyhunt.R
import com.moengage.dailyhunt.core.data.model.NewsArticle
import com.moengage.dailyhunt.databinding.LayoutNewsArticleItemBinding
import com.moengage.dailyhunt.utils.getFormattedNewsArticleTime

class NewsArticleRecyclerAdapter(
    private val newsArticles: List<NewsArticle>, private val listener: NewsArticleRecyclerListener
) : RecyclerView.Adapter<NewsArticleRecyclerAdapter.ViewHolder>() {

    interface NewsArticleRecyclerListener {
        fun onClickRead(position: Int, article: NewsArticle)
    }

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

        fun bind(article: NewsArticle, listener: NewsArticleRecyclerListener) {
            with(viewBinding) {
                newsTitle.text = article.title
                newsDescription.text = article.description
                newsPublishedAt.text = getFormattedNewsArticleTime(article.publishedAt)
                Glide.with(newsImage)
                    .load(article.imageUrl)
                    .placeholder(R.drawable.placeholder_no_image_found)
                    .into(newsImage)
                newsRead.setOnClickListener {
                    listener.onClickRead(absoluteAdapterPosition, article)
                }
                root.setOnClickListener {
                    listener.onClickRead(absoluteAdapterPosition, article)
                }
            }
        }
    }
}