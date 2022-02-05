package com.callmonitor.network.model.reponse

import com.squareup.moshi.Json

data class SaveCallStatusNetworkResponse(
    @field:Json(name = "status") val status: Int = 0,
    @field:Json(name = "message") val message: String = "",
    @field:Json(name = "isRecordSaved") val isRecordSaved: Boolean = false
)
