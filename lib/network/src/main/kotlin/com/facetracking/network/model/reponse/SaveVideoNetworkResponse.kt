package com.facetracking.network.model.reponse

import com.squareup.moshi.Json

data class SaveVideoNetworkResponse(
    @field:Json(name = "status") val status: Int = 0,
    @field:Json(name = "message") val message: String = "",
    @field:Json(name = "isVideoSaved") val isVideoSaved: Boolean = false
)
