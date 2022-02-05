package com.callmonitor.data.usecase

import com.callmonitor.data.repository.CallMonitorRepository
import com.callmonitor.model.CallStatus
import javax.inject.Inject

class SaveCallStatusUseCase @Inject constructor(
    private val callMonitorRepository: CallMonitorRepository
) {

    suspend fun saveCallStatus(callStatus: CallStatus) =
        callMonitorRepository.saveCallStatus(callStatus)
}
