package com.callmonitor.model

data class Root(
    val start: String,
    val services: List<ServiceStatus>
)
