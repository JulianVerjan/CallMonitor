package com.facetracking.model

data class SaveVideoResult(
    val status: Int = 0,
    val message: String = "",
    val isVideoSaved: Boolean = false
)
