package com.facetracking.video.tracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facetracking.video.model.UIState
import com.facetracking.data.usecase.SaveVideoUseCase
import com.facetracking.network.model.mapper.RepositoryResult
import com.facetracking.video.model.ProgressRequestBody
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

@HiltViewModel
class FaceTrackingViewModel @Inject constructor(
    private val saveVideoUseCase: SaveVideoUseCase
) : ViewModel() {

    private val _viewStateFlow = MutableStateFlow(FaceTrackingViewState())
    val viewStateFlow: StateFlow<FaceTrackingViewState> = _viewStateFlow

    fun saveVideo(requestFile: ProgressRequestBody) {
        viewModelScope.launch {
            _viewStateFlow.value = viewStateFlow.value.copy(uiState = UIState.LOADING)
            val body = MultipartBody.Part.createFormData("uploaded_file",
                    requestFile.file.name,requestFile)
            when (val saveVideoResult = saveVideoUseCase.saveVideo(body)) {
                is RepositoryResult.Success -> {
                    _viewStateFlow.value = FaceTrackingViewState(
                        uiState = if (saveVideoResult.result != null) UIState.CONTENT else UIState.ERROR,
                    )
                }
                else -> {
                    _viewStateFlow.value = FaceTrackingViewState(uiState = UIState.ERROR)
                }
            }
        }
    }
}
