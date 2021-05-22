package com.facetracking.data.repository

import com.facetracking.data.datasource.FaceTrackingRemoteDataSource
import com.facetracking.model.SaveVideoResult
import com.facetracking.network.model.mapper.RepositoryResult
import com.facetracking.network.model.mapper.toRepositoryResult
import com.facetracking.network.model.reponse.SaveVideoNetworkResponse
import com.facetracking.network.model.mapper.toVideoModel
import javax.inject.Inject

class FaceTrackingRepository @Inject constructor(
    private val faceTrackingRemoteDataSource: FaceTrackingRemoteDataSource,
) {

    suspend fun saveVideo(): RepositoryResult<SaveVideoResult> =
        faceTrackingRemoteDataSource.saveVideo()
            .toRepositoryResult(SaveVideoNetworkResponse::toVideoModel)
}
