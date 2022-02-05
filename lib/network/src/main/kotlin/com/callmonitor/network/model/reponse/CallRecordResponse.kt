package com.callmonitor.network.model.reponse

data class CallRecordResponse(
    val name: String = "",
    val number: String = "",
    val timeQueried: Int = 0,
    val beginning: String = "",
    val duration: String = ""
)
