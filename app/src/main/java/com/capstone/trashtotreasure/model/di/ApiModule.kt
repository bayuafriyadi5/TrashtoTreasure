package com.capstone.trashtotreasure.model.di

import com.capstone.trashtotreasure.model.data.remote.retrofit.ApiConfig
import com.capstone.trashtotreasure.model.data.remote.retrofit.ApiService
import com.capstone.trashtotreasure.model.data.remote.retrofit.ApiServiceML
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideApiService(): ApiService = ApiConfig.getApiService()

    @Provides
    @Singleton
    fun provideApiServiceML(): ApiServiceML {
        return ApiConfig.getApiServiceML()
    }

}