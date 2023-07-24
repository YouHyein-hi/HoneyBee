package com.example.domain.usecase.data

import android.graphics.Bitmap
import com.example.domain.repo.GeneralRepository

/**
 * 2023-07-23
 * pureum
 */
class GetPictureDataUseCase(
    private val generalRepository: GeneralRepository
) {
    suspend operator fun invoke(uid:String) : Bitmap {
        return generalRepository.getPictureDataUseCaseRepository(uid)
    }
}