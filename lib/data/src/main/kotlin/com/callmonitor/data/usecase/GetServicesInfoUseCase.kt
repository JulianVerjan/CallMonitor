package com.callmonitor.data.usecase

import com.callmonitor.data.repository.CallMonitorRepository
import javax.inject.Inject

class GetServicesInfoUseCase @Inject constructor(
        private val callMonitorRepository: CallMonitorRepository
) {

    suspend fun getServicesInfoUseCase() =
            callMonitorRepository.getCallServicesInfo()
}
