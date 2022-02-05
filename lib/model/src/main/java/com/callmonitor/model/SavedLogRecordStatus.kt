package com.callmonitor.model

data class SavedLogRecordStatus(
    val callStatusToDelete: CallStatus,
    val callRecord: CallRecord
)
