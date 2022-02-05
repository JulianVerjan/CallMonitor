package com.callmonitor.network.service

import com.callmonitor.model.SavedLogRecordStatus
import com.callmonitor.model.CallStatus
import com.callmonitor.model.CallRecord
import com.callmonitor.model.Root
import com.callmonitor.model.SavedRecordStatus
import com.callmonitor.network.model.NetworkResponse
import com.callmonitor.network.model.reponse.CallRecordResponse
import com.callmonitor.network.model.reponse.GetServicesInfoNetworkResponse
import com.callmonitor.network.model.reponse.SaveCallStatusNetworkResponse
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Url
import retrofit2.http.Body
import retrofit2.http.Headers

interface CallMonitorApi {

    @Headers("Content-Type: application/json")
    @POST
    suspend fun saveCallStatus(@Url url: String, @Body callStatus: CallStatus):
            NetworkResponse<SaveCallStatusNetworkResponse, SavedRecordStatus>

    @GET("")
    suspend fun getCallRecordList(@Url url: String):
            NetworkResponse<List<CallRecordResponse>, List<CallRecord>>

    @GET
    suspend fun getCallServicesInfo(@Url url: String):
            NetworkResponse<GetServicesInfoNetworkResponse, Root>

    @Headers("Content-Type: application/json")
    @POST
    suspend fun saveCallRecord(@Url url: String, @Body callRecord: SavedLogRecordStatus):
            NetworkResponse<SaveCallStatusNetworkResponse, SavedRecordStatus>
}
