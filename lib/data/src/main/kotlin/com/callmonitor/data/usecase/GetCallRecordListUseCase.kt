package com.callmonitor.data.usecase

import com.callmonitor.data.repository.CallMonitorRepository
import javax.inject.Inject

class GetCallRecordListUseCase @Inject constructor(
        private val callMonitorRepository: CallMonitorRepository
) {

    suspend fun getCallRecordList() =
            callMonitorRepository.getCallRecordList()
}
