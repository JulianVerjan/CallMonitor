package com.facetracking.data.datasource

import com.facetracking.network.service.FaceTrackingService
import javax.inject.Inject

class FaceTrackingRemoteDataSource @Inject constructor(
    private val faceTrackingService: FaceTrackingService
) {

    suspend fun saveVideo() = faceTrackingService.saveVideo()
}
