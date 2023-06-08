package com.capstone.trashtotreasure.model.data.local.room

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.capstone.trashtotreasure.model.data.local.entitiy.NewsEntity

@Dao
interface NewsDao {
    @Query("SELECT * FROM news")
    fun getNews(): PagingSource<Int, NewsEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(vararg news: NewsEntity)

    @Update
    fun updateNews(news: NewsEntity)

    @Query("DELETE FROM news")
    fun deleteAll()




}