package com.example.data.module

import com.example.domain.repo.ReceiveRepo
import com.example.domain.repo.SendRepo
import com.example.domain.usecase.ReceiveUseCase
import com.example.domain.usecase.SendUseCase
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
    fun provideUseCaseSendData(repo : SendRepo) : SendUseCase{
        return SendUseCase(repo)
    }
    @Provides
    @Singleton
    fun provideUseCaseReceiveData(repo : ReceiveRepo) : ReceiveUseCase{
        return ReceiveUseCase(repo)
    }
}