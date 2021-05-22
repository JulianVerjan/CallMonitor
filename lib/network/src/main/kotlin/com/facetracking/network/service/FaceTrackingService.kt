package com.facetracking.network.service

import com.facetracking.network.model.NetworkResponse
import com.facetracking.network.model.reponse.SaveVideoNetworkResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface FaceTrackingService {

    @POST("25960fec-fdb0-468a-9227-0e2768d757b5")
    suspend fun saveVideo(@Body video: String = ""):
            NetworkResponse<SaveVideoNetworkResponse, Any>
}
