package com.example.data.module

import com.example.domain.repo.RetrofitRepo
import com.example.domain.repo.RoomRepo
import com.example.domain.usecase.RetrofitUseCase
import com.example.domain.usecase.RoomUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 2023-02-02
 * pureum
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideUseCaseReceiveData(repo : RetrofitRepo) : RetrofitUseCase{
        return RetrofitUseCase(repo)
    }

    @Provides
    @Singleton
    fun provideUseCaseRoomData(repo : RoomRepo) : RoomUseCase{
        return RoomUseCase(repo)
    }
}