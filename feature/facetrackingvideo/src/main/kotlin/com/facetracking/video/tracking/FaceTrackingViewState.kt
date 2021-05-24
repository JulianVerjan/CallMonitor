package com.facetracking.video.tracking

import com.facetracking.video.model.UIState

data class FaceTrackingViewState(
        val uiState: UIState = UIState.IDLE
)
