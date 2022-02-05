package com.callmonitor.task.model

import com.callmonitor.model.CallStatus
import com.callmonitor.model.Root

data class CallStatusViewState(
    val uiState: UIState = UIState.IDLE, val servicesList: Root? = null,
    val callStatus: CallStatus? = null
)
