package com.callmonitor.task.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.callmonitor.data.usecase.GetCallRecordListUseCase
import com.callmonitor.data.usecase.GetServicesInfoUseCase
import com.callmonitor.task.model.UIState
import com.callmonitor.network.model.mapper.RepositoryResult
import com.callmonitor.task.model.CallLogViewState
import com.callmonitor.task.model.CallStatusViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CallMonitorViewModel @Inject constructor(
    private val getCallRecordListUseCase: GetCallRecordListUseCase,
    private val getServicesInfoUseCase: GetServicesInfoUseCase
) : ViewModel() {

    private val _viewStateFlow = MutableStateFlow(CallStatusViewState())
    val viewStateFlow: StateFlow<CallStatusViewState> = _viewStateFlow

    private val _viewStateCallLogFlow = MutableStateFlow(CallLogViewState())
    val viewStateCallLogFlow: StateFlow<CallLogViewState> = _viewStateCallLogFlow

    fun getServicesList() {
        viewModelScope.launch {
            _viewStateFlow.value = viewStateFlow.value.copy(uiState = UIState.LOADING)
            when (val getServicesListResult = getServicesInfoUseCase.getServicesInfoUseCase()) {
                is RepositoryResult.Success -> {
                    _viewStateFlow.value = CallStatusViewState(
                        uiState = if (getServicesListResult.result != null) UIState.SERVICES_CONTENT else UIState.ERROR,
                        servicesList = getServicesListResult.result)
                }
                else -> {
                    _viewStateFlow.value = CallStatusViewState(uiState = UIState.ERROR)
                }
            }
        }
    }

    fun getCallLog() {
        viewModelScope.launch {
            _viewStateCallLogFlow.value = viewStateCallLogFlow.value.copy(uiState = UIState.LOADING)
            when (val callLogList =
                getCallRecordListUseCase.getCallRecordList()) {
                is RepositoryResult.Success -> {
                    _viewStateCallLogFlow.value = CallLogViewState(
                        uiState = if (callLogList.result != null) UIState.CALLS_CONTENT else UIState.ERROR,
                        recordedCallsList = callLogList.result ?: listOf()
                    )
                }
                else -> {
                    _viewStateCallLogFlow.value = CallLogViewState(uiState = UIState.ERROR)
                }
            }
        }
    }
}
