package com.callmonitor.task.model

import com.callmonitor.model.CallRecord

data class CallLogViewState(
    val uiState: UIState = UIState.IDLE,
    val recordedCallsList: List<CallRecord> = listOf()
)
