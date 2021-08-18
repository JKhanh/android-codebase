package com.aibles.home.di

import com.aibles.home.data.remote.GithubService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object HomeRemoteModule {

    @Singleton
    @Provides
    fun provideGithubService(retrofit: Retrofit) =
        retrofit.create(GithubService::class.java)
}

