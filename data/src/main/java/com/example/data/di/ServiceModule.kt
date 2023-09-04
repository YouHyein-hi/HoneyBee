//package com.example.data.di
//
//import com.example.data.remote.dataSource.CardDataSource
//import com.example.data.remote.dataSource.GeneralDataSource
//import com.example.data.remote.dataSource.LoginDataSource
//import com.example.data.remote.dataSource.NoticeDataSource
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import retrofit2.Retrofit
//import javax.inject.Singleton
//
///**
// * 2023-09-04
// * pureum
// */
//@Module
//@InstallIn(SingletonComponent::class)
//object ServiceModule {
//    @Singleton
//    @Provides
//    fun provideLoginService(@RetrofitModule.Login retrofit: Retrofit): LoginDataSource =
//        retrofit.create(LoginDataSource::class.java)
//
//    @Singleton
//    @Provides
//    fun provideCardService(@RetrofitModule.Api retrofit: Retrofit): CardDataSource =
//        retrofit.create(CardDataSource::class.java)
//
//    @Singleton
//    @Provides
//    fun provideGeneralService(@RetrofitModule.Api retrofit: Retrofit): GeneralDataSource =
//        retrofit.create(GeneralDataSource::class.java)
//
//    @Singleton
//    @Provides
//    fun provideNoticeService(@RetrofitModule.Api retrofit: Retrofit): NoticeDataSource =
//        retrofit.create(NoticeDataSource::class.java)
//}