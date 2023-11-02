package com.moengage.dailyhunt.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.moengage.dailyhunt.core.data.model.NewsArticle
import com.moengage.dailyhunt.core.data.model.SortOrder
import com.moengage.dailyhunt.core.data.repository.NewsRepository
import com.moengage.dailyhunt.core.data.repository.PrefRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val newsRepository: NewsRepository, private val prefRepository: PrefRepository
) : ViewModel() {

    fun getNewsArticles(): LiveData<List<NewsArticle>> {
        return liveData {
            emit(newsRepository.getNewsArticles())
        }
    }

    fun getSortedArticles(sortOrder: SortOrder): LiveData<List<NewsArticle>> {
        return liveData {
            when (sortOrder) {
                SortOrder.DESCENDING -> {
                    emit(newsRepository.getNewsArticles().sortedByDescending { it.publishedAt })
                }

                SortOrder.ASCENDING -> {
                    emit(newsRepository.getNewsArticles().sortedBy { it.publishedAt })
                }
            }
        }
    }

    fun getNotificationRequestCount(): Int {
        return prefRepository.getNotificationRequestCount()
    }

    fun updateNotificationRequestCount() {
        prefRepository.incrementNotificationRequestCount()
    }
}