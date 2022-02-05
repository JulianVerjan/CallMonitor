package com.callmonitor.data.usecase

import com.callmonitor.data.repository.CallMonitorRepository
import com.callmonitor.model.CallRecord
import com.callmonitor.model.CallStatus
import javax.inject.Inject

class SaveCallRecordUseCase @Inject constructor(
    private val callMonitorRepository: CallMonitorRepository,
) {

    suspend fun saveCallRecord(
        callStatusToDelete: CallStatus,
        callRecord: CallRecord
    ) =
        callMonitorRepository.saveCallRecord(callStatusToDelete, callRecord)
}
