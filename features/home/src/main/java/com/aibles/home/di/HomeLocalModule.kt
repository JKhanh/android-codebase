package com.aibles.home.di

import android.content.Context
import com.aibles.home.data.local.UserDao
import com.aibles.home.data.local.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object HomeLocalModule {

    @Singleton
    @Provides
    fun provideUserDatabase(@ApplicationContext context: Context): UserDatabase =
        UserDatabase.buildDatabase(context)

    @Singleton
    @Provides
    fun provideUserDao(database: UserDatabase): UserDao = database.userDao()
}