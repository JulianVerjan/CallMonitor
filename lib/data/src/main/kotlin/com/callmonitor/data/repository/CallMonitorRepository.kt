package com.callmonitor.data.repository

import com.callmonitor.data.datasource.CallMonitorRemoteDataSource
import com.callmonitor.model.CallStatus
import com.callmonitor.model.Root
import com.callmonitor.model.SavedRecordStatus
import com.callmonitor.model.CallRecord
import com.callmonitor.model.SavedLogRecordStatus
import com.callmonitor.network.model.mapper.toSavedRecordModel
import com.callmonitor.network.model.mapper.RepositoryResult
import com.callmonitor.network.model.mapper.toRepositoryResult
import com.callmonitor.network.model.mapper.toRootModel
import com.callmonitor.network.model.mapper.toCallRecordList
import com.callmonitor.network.model.reponse.CallRecordResponse
import com.callmonitor.network.model.reponse.SaveCallStatusNetworkResponse
import com.callmonitor.network.model.reponse.GetServicesInfoNetworkResponse
import javax.inject.Inject

class CallMonitorRepository @Inject constructor(
    private val callMonitorRemoteDataSource: CallMonitorRemoteDataSource,
) {

    suspend fun saveCallStatus(callStatus: CallStatus): RepositoryResult<SavedRecordStatus> =
        callMonitorRemoteDataSource.saveCallStatus(callStatus)
            .toRepositoryResult(SaveCallStatusNetworkResponse::toSavedRecordModel)

    suspend fun getCallRecordList(): RepositoryResult<List<CallRecord>> =
        callMonitorRemoteDataSource.getCallRecordList()
            .toRepositoryResult(List<CallRecordResponse>::toCallRecordList)

    suspend fun getCallServicesInfo(): RepositoryResult<Root> =
        callMonitorRemoteDataSource.getCallServicesInfo()
            .toRepositoryResult(GetServicesInfoNetworkResponse::toRootModel)

    suspend fun saveCallRecord(
        callStatusToDelete: CallStatus,
        callRecord: CallRecord
    ): RepositoryResult<SavedRecordStatus> =
        callMonitorRemoteDataSource.saveCallRecord(SavedLogRecordStatus(callStatusToDelete,
            callRecord))
            .toRepositoryResult(SaveCallStatusNetworkResponse::toSavedRecordModel)

}
