package com.facetracking.data.repository

import com.facetracking.data.datasource.FaceTrackingRemoteDataSource
import com.facetracking.model.SaveVideoResult
import com.facetracking.network.model.mapper.RepositoryResult
import com.facetracking.network.model.mapper.toRepositoryResult
import com.facetracking.network.model.reponse.SaveVideoNetworkResponse
import com.facetracking.network.model.mapper.toVideoModel
import okhttp3.MultipartBody
import javax.inject.Inject

class FaceTrackingRepository @Inject constructor(
        private val faceTrackingRemoteDataSource: FaceTrackingRemoteDataSource,
) {

    suspend fun saveVideo(video: MultipartBody.Part): RepositoryResult<SaveVideoResult> =
            faceTrackingRemoteDataSource.saveVideo(video)
                    .toRepositoryResult(SaveVideoNetworkResponse::toVideoModel)
}
