package com.capstone.trashtotreasure.view.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.capstone.trashtotreasure.model.data.local.entitiy.NewsEntity
import com.capstone.trashtotreasure.model.data.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
@ExperimentalPagingApi
class HomeViewModel @Inject constructor(
    private val articleRepository: ArticleRepository
): ViewModel() {
    fun getAllArticle(): LiveData<PagingData<NewsEntity>> =
        articleRepository.getArticle()

}