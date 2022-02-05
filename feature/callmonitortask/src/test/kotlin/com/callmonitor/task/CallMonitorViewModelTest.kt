package com.callmonitor.task

import com.callmonitor.data.usecase.GetCallRecordListUseCase
import com.callmonitor.data.usecase.GetServicesInfoUseCase
import com.callmonitor.model.CallRecord
import com.callmonitor.model.Root
import com.callmonitor.model.ServiceStatus
import com.callmonitor.network.model.mapper.RepositoryResult
import com.callmonitor.task.model.UIState
import com.callmonitor.task.viewmodel.CallMonitorViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.mockk.MockKAnnotations
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CallMonitorViewModelTest {

    private val dispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(dispatcher)

    private var getServicesInfoUseCase = mock<GetServicesInfoUseCase>()
    private var getCallRecordListUseCase = mock<GetCallRecordListUseCase>()

    private var callMonitorViewModel =
        CallMonitorViewModel(getCallRecordListUseCase, getServicesInfoUseCase)

    private val serviceList =
        Root(start = "Test Date", listOf(ServiceStatus(name = "test", url = "http://www.bla.com")))

    private val callLogList = listOf(CallRecord(name = "Pepe", number = "+1 45553454",
        timeQueried = 0,
        beginning = "",
        duration = "50"))

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        // Clean up the TestCoroutineDispatcher to make sure no other work is running.
        dispatcher.cleanupTestCoroutines()
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun fetServicesListSuccessfully() {
        testScope.runBlockingTest {
            whenever(getServicesInfoUseCase.getServicesInfoUseCase())
                .thenReturn(RepositoryResult.Success(serviceList))

            val job = launch {
                callMonitorViewModel.getServicesList()
                callMonitorViewModel.viewStateFlow.collect {
                    assertEquals(it.uiState, UIState.SERVICES_CONTENT)
                    assertEquals(it.servicesList, serviceList)
                }
            }
            job.cancel()
        }
    }

    @Test
    fun fetchServicesListWithError() {
        testScope.runBlockingTest {
            whenever(getServicesInfoUseCase.getServicesInfoUseCase())
                .thenReturn(RepositoryResult.Fail("error"))
            val job = launch {
                callMonitorViewModel.getServicesList()
                callMonitorViewModel.viewStateFlow.collect {
                    assertEquals(it.uiState, UIState.ERROR)
                }
            }
            job.cancel()
        }
    }

    @Test
    fun fetchServicesListApiError() {
        testScope.runBlockingTest {
            whenever(getServicesInfoUseCase.getServicesInfoUseCase())
                .thenReturn(RepositoryResult.Exception(Exception()))
            val job = launch {
                callMonitorViewModel.getServicesList()
                callMonitorViewModel.viewStateFlow.collect {
                    assertEquals(it.uiState, UIState.ERROR)
                }
            }
            job.cancel()
        }
    }

    @Test
    fun fetCallLogListSuccessfully() {
        testScope.runBlockingTest {
            whenever(getCallRecordListUseCase.getCallRecordList())
                .thenReturn(RepositoryResult.Success(callLogList))

            val job = launch {
                callMonitorViewModel.getCallLog()
                callMonitorViewModel.viewStateCallLogFlow.collect {
                    assertEquals(it.uiState, UIState.CALLS_CONTENT)
                    assertEquals(it.recordedCallsList, callLogList)
                }
            }
            job.cancel()
        }
    }

    @Test
    fun fetchCallLogListWithError() {
        testScope.runBlockingTest {
            whenever(getCallRecordListUseCase.getCallRecordList())
                .thenReturn(RepositoryResult.Fail("error"))
            val job = launch {
                callMonitorViewModel.getCallLog()
                callMonitorViewModel.viewStateCallLogFlow.collect {
                    assertEquals(it.uiState, UIState.ERROR)
                }
            }
            job.cancel()
        }
    }

    @Test
    fun fetchCallLogListApiError() {
        testScope.runBlockingTest {
            whenever(getCallRecordListUseCase.getCallRecordList())
                .thenReturn(RepositoryResult.Exception(Exception()))
            val job = launch {
                callMonitorViewModel.getCallLog()
                callMonitorViewModel.viewStateCallLogFlow.collect {
                    assertEquals(it.uiState, UIState.ERROR)
                }
            }
            job.cancel()
        }
    }
}
