package com.callmonitor.network.model.reponse

data class GetServicesInfoNetworkResponse(
    val start: String,
    val services: List<ServiceStatusResponse>
)
