package com.callmonitor.data.datasource

import com.callmonitor.model.CallStatus
import com.callmonitor.model.SavedLogRecordStatus
import com.callmonitor.network.BuildConfig
import com.callmonitor.network.service.CallMonitorApi
import com.test.server.server.CallMonitorServer
import javax.inject.Inject

class CallMonitorRemoteDataSource @Inject constructor(
    private val callMonitorApi: CallMonitorApi
) {
    suspend fun saveCallStatus(callStatus: CallStatus) =
        callMonitorApi.saveCallStatus("${BuildConfig.API_BASE_URL}/save_status", callStatus)

    suspend fun getCallRecordList() =
        callMonitorApi.getCallRecordList("${BuildConfig.API_BASE_URL}/log")

    suspend fun getCallServicesInfo() =
        callMonitorApi.getCallServicesInfo("${BuildConfig.API_BASE_URL}/")

    suspend fun saveCallRecord(callRecord: SavedLogRecordStatus) =
        callMonitorApi.saveCallRecord("${BuildConfig.API_BASE_URL}/save_log_record", callRecord)
}
