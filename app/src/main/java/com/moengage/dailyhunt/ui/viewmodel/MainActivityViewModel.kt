package com.moengage.dailyhunt.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.moengage.dailyhunt.core.data.model.NewsArticle
import com.moengage.dailyhunt.core.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val newsRepository: NewsRepository) :
    ViewModel() {

    fun getNewsArticles(): LiveData<List<NewsArticle>> {
        return liveData {
            emit(newsRepository.getNewsArticles())
        }
    }
}