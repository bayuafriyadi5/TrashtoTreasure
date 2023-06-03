package com.capstone.trashtotreasure.model.di

import android.content.Context
import androidx.room.Room
import com.capstone.trashtotreasure.model.data.local.room.NewsDao
import com.capstone.trashtotreasure.model.data.local.room.NewsDatabase
import com.capstone.trashtotreasure.model.data.local.room.RemoteKeysDao
import com.capstone.trashtotreasure.utils.AppExecutors
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideNewsDao(newsDatabase: NewsDatabase): NewsDao = newsDatabase.newsDao()

    @Provides
    fun provideAppExecutors() = AppExecutors()

    @Provides
    fun provideRemoteKeysDao(newsDatabase: NewsDatabase): RemoteKeysDao =
        newsDatabase.remoteKeysDao()

    @Provides
    @Singleton
    fun provideNewsDatabase(@ApplicationContext context: Context): NewsDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            NewsDatabase::class.java,
            "news.db"
        ).build()
}