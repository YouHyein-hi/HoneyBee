package com.example.data.di

import android.content.Context
import android.content.SharedPreferences
import com.example.data.manager.PreferenceManager
import com.example.data.util.HeaderManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 2023-06-28
 * pureum
 */
@Module
@InstallIn(SingletonComponent::class)
object AppDataModule {

    @Singleton
    @Provides
    fun provideHeaderManager(preferenceManager: PreferenceManager): HeaderManager =
        HeaderManager(preferenceManager)

}