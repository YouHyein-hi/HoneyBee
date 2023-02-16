package com.example.data.module

import android.content.Context
import androidx.room.Room
import com.example.data.local.LocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 2023-02-15
 * pureum
 */
@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {
    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext appContext: Context): LocalDatabase{
        return Room.databaseBuilder(
            appContext, LocalDatabase::class.java, "myDB"
        )//.fallbackToDestructiveMigration()
            // => 이걸쓰면 테이블이 유실되어 호출 실패해도 db를 재생성함(이전데이터 날라감)
        .build()
    }
}