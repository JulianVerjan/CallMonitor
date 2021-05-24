package com.facetracking.data.usecase

import com.facetracking.data.repository.FaceTrackingRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class SaveVideoUseCase @Inject constructor(
        private val faceTrackingRepository: FaceTrackingRepository
) {

    suspend fun saveVideo(video: MultipartBody.Part) =
            faceTrackingRepository.saveVideo(video)
}
