package com.example.data.di

import com.example.domain.repo.*
import com.example.domain.usecase.card.GetCardListUseCase
import com.example.domain.usecase.card.InsertCardUseCase
import com.example.domain.usecase.bill.*
import com.example.domain.usecase.card.GetCardSpinnerUseCase
import com.example.domain.usecase.login.LoginUseCase
import com.example.domain.usecase.notice.AddNoticeUseCase
import com.example.domain.usecase.notice.GetNoticeListUseCase
import com.example.domain.usecase.room.DeleteRoomDataUseCase
import com.example.domain.usecase.room.GetRoomDataListUseCase
import com.example.domain.usecase.room.InsertRoomDataUseCase
import com.example.domain.usecase.room.UpdateRoomDataUseCase
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
    fun provideGetCardListUseCase(repo : CardRepository) : GetCardListUseCase{
        return GetCardListUseCase(repo)
    }

    @Provides
    @Singleton
    fun provideGetCardSpinnerUseCase(repo: CardRepository) : GetCardSpinnerUseCase{
        return GetCardSpinnerUseCase(repo)
    }

    @Provides
    @Singleton
    fun provideInsertCardUseCase(repo : CardRepository) : InsertCardUseCase{
        return InsertCardUseCase(repo)
    }

    //카드 업데이트 기능은 아직 미사용
//    @Provides
//    @Singleton
//    fun provideUpdateCardUseCase(repo : CardRepository) : UpdateCardUseCase {
//        return UpdateCardUseCase(repo)
//    }

    @Provides
    @Singleton
    fun provideDeleteDataUseCase(repo : GeneralRepository) : DeleteDataUseCase{
        return DeleteDataUseCase(repo)
    }

    @Provides
    @Singleton
    fun providePictureDataUseCase(repo : GeneralRepository) : GetPictureDataUseCase{
        return GetPictureDataUseCase(repo)
    }

    @Provides
    @Singleton
    fun provideGetDataListUseCase(repo : GeneralRepository) : GetDataListUseCase{
        return GetDataListUseCase(repo)
    }

    @Provides
    @Singleton
    fun provideGetDetailDataUseCase(repo : GeneralRepository) : GetDetailDataUseCase{
        return GetDetailDataUseCase(repo)
    }


    @Provides
    @Singleton
    fun provideGetStoreListUseCase(repo : GeneralRepository) : GetStoreListUseCase{
        return GetStoreListUseCase(repo)
    }

    @Provides
    @Singleton
    fun provideInsertDataUseCase(repo : GeneralRepository) : InsertDataUseCase{
        return InsertDataUseCase(repo)
    }

    @Provides
    @Singleton
    fun provideUpdateDataUseCase(repo : GeneralRepository) : UpdateDataUseCase{
        return UpdateDataUseCase(repo)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(repo : LoginRepository) : LoginUseCase{
        return LoginUseCase(repo)
    }

    //Notice
    @Provides
    @Singleton
    fun provideGetNoticeListUseCase(noticeRepo : NoticeRepository) : GetNoticeListUseCase{
        return GetNoticeListUseCase(noticeRepo)
    }

    @Provides
    @Singleton
    fun provideAddNoticeUseCase(noticeRepo : NoticeRepository) : AddNoticeUseCase{
        return AddNoticeUseCase(noticeRepo)
    }



    //ROOM
    @Provides
    @Singleton
    fun provideDeleteRoomDataUseCase(repo : RoomRepository) : DeleteRoomDataUseCase{
        return DeleteRoomDataUseCase(repo)
    }

    @Provides
    @Singleton
    fun provideGetRoomDataListUseCase(repo : RoomRepository) : GetRoomDataListUseCase{
        return GetRoomDataListUseCase(repo)
    }

    @Provides
    @Singleton
    fun provideInsertDataRoomUseCase(repo : RoomRepository) : InsertRoomDataUseCase{
        return InsertRoomDataUseCase(repo)
    }

    @Provides
    @Singleton
    fun provideUpdateRoomUseCAse(repo : RoomRepository) : UpdateRoomDataUseCase{
        return UpdateRoomDataUseCase(repo)
    }


}