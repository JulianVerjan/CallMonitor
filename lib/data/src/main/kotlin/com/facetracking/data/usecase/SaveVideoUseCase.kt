package com.facetracking.data.usecase

import com.facetracking.data.repository.FaceTrackingRepository
import javax.inject.Inject

class SaveVideoUseCase @Inject constructor(
    private val faceTrackingRepository: FaceTrackingRepository
) {

    suspend fun saveVideo() = faceTrackingRepository.saveVideo()
}
