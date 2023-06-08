package com.capstone.trashtotreasure.model.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.capstone.trashtotreasure.model.data.local.room.NewsDao
import com.capstone.trashtotreasure.model.data.local.room.NewsDatabase
import com.capstone.trashtotreasure.model.data.local.entitiy.NewsEntity
import com.capstone.trashtotreasure.model.data.local.room.ArticleRemoteMediator
import com.capstone.trashtotreasure.model.data.remote.retrofit.ApiService
import com.capstone.trashtotreasure.utils.AppExecutors
import javax.inject.Inject

@ExperimentalPagingApi
class ArticleRepository @Inject constructor(
    private val apiService: ApiService,
    private val newsDao: NewsDao,
    private val newsDatabase: NewsDatabase,
    private val appExecutors: AppExecutors
) {

    fun getArticle(): LiveData<PagingData<NewsEntity>> =

        Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            remoteMediator = ArticleRemoteMediator(
                apiService,
                newsDatabase
            ),
            pagingSourceFactory = {
                newsDatabase.newsDao().getNews()
            }
        ).liveData


}