package com.facetracking.network.service

import com.facetracking.network.model.NetworkResponse
import com.facetracking.network.model.reponse.SaveVideoNetworkResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FaceTrackingService {

    @Multipart
    @POST("25960fec-fdb0-468a-9227-0e2768d757b5")
    suspend fun saveVideo(@Part file: MultipartBody.Part):
            NetworkResponse<SaveVideoNetworkResponse, Any>
}
