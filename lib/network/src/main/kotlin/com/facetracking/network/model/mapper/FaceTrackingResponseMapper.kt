package com.facetracking.network.model.mapper

import com.facetracking.model.SaveVideoResult
import com.facetracking.network.model.reponse.SaveVideoNetworkResponse

fun SaveVideoNetworkResponse.toVideoModel() = SaveVideoResult(
    status = this.status,
    message = this.message,
    isVideoSaved = this.isVideoSaved
)
