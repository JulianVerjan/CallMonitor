package com.callmonitor.model

data class SavedRecordStatus(
    val status: Int = 0,
    val message: String = "",
    val isRecordSaved: Boolean = false
)
