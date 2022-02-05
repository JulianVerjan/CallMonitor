package com.callmonitor.model

data class CallRecord(
    val name: String = "",
    val number: String = "",
    val timeQueried: Int = 0,
    val beginning: String = "",
    val duration: String = ""
)
