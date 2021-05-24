package com.facetracking.data.datasource

import com.facetracking.network.service.FaceTrackingService
import okhttp3.MultipartBody
import javax.inject.Inject

class FaceTrackingRemoteDataSource @Inject constructor(
        private val faceTrackingService: FaceTrackingService
) {
    suspend fun saveVideo(video: MultipartBody.Part) =
            faceTrackingService.saveVideo(video)
}
