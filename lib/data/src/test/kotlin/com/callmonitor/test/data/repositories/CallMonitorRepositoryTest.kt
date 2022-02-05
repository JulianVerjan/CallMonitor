package com.callmonitor.test.data.repositories

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.callmonitor.data.datasource.CallMonitorRemoteDataSource
import com.callmonitor.data.repository.CallMonitorRepository
import com.callmonitor.model.CallRecord
import com.callmonitor.model.CallStatus
import com.callmonitor.model.SavedLogRecordStatus
import com.callmonitor.model.SavedRecordStatus
import com.callmonitor.network.model.NetworkResponse
import com.callmonitor.network.model.mapper.RepositoryResult
import com.callmonitor.network.model.reponse.CallRecordResponse
import com.callmonitor.network.model.reponse.SaveCallStatusNetworkResponse
import com.callmonitor.network.service.CallMonitorApi
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CallMonitorRepositoryTest {

    @MockK(relaxed = true)
    lateinit var callMonitorApi: CallMonitorApi
    private lateinit var callMonitorRepository: CallMonitorRepository
    private lateinit var callMonitorRemoteDataSource: CallMonitorRemoteDataSource
    private val mockData = SaveCallStatusNetworkResponse(
        status = 200,
        message = "Info saved",
        isRecordSaved = true
    )

    private val callStatusMock = CallStatus("Pepe", "12345678", true)
    private val callRecordMock = CallRecord("Pepe", "12345678", 0, "DD-MM-YY", "56")

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        callMonitorApi = mock()
        callMonitorRemoteDataSource = CallMonitorRemoteDataSource(callMonitorApi)
        callMonitorRepository = CallMonitorRepository(callMonitorRemoteDataSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun saveCallStatusSuccessfully() {
        runBlockingTest {

            whenever(callMonitorRemoteDataSource.saveCallStatus(callStatusMock))
                .thenReturn(NetworkResponse.Success(mockData))
            whenever(callMonitorApi.saveCallStatus("test_url", callStatusMock))
                .thenReturn(NetworkResponse.Success(mockData))

            val response = callMonitorRepository.saveCallStatus(callStatusMock)
            assert(response is RepositoryResult.Success)
            val successResponse = response as RepositoryResult.Success
            assertEquals(successResponse.result?.isRecordSaved, true)
        }
    }

    @Test
    fun saveCallStatusWithApiError() {
        runBlockingTest {
            whenever(callMonitorRemoteDataSource.saveCallStatus(callStatusMock))
                .thenReturn(NetworkResponse.ApiError(SavedRecordStatus(status = 405,
                    message = "error",
                    isRecordSaved = false), 405))
            whenever(callMonitorApi.saveCallStatus("test_url", callStatusMock))
                .thenReturn(NetworkResponse.ApiError(SavedRecordStatus(status = 405,
                    message = "error",
                    isRecordSaved = false), 405))
            val response = callMonitorRepository.saveCallStatus(callStatusMock)
            assertNotNull(response)
            assert(response is RepositoryResult.Fail<*>)
        }
    }

    @Test
    fun saveCallStatusNetworkError() {
        runBlockingTest {
            whenever(callMonitorRemoteDataSource.saveCallStatus(callStatusMock))
                .thenReturn(NetworkResponse.NetworkError(IOException()))
            whenever(callMonitorApi.saveCallStatus("test_url", callStatusMock))
                .thenReturn(NetworkResponse.NetworkError(IOException()))
            val response = callMonitorRepository.saveCallStatus(callStatusMock)
            assertNotNull(response)
            assert(response is RepositoryResult.Exception)
        }
    }

    @Test
    fun saveCallRecordSuccessfully() {
        runBlockingTest {

            whenever(callMonitorRemoteDataSource.saveCallRecord(SavedLogRecordStatus(callStatusMock,
                callRecordMock)))
                .thenReturn(NetworkResponse.Success(mockData))
            whenever(callMonitorApi.saveCallStatus("test_url", callStatusMock))
                .thenReturn(NetworkResponse.Success(mockData))

            val response = callMonitorRepository.saveCallRecord(callStatusMock, callRecordMock)
            assert(response is RepositoryResult.Success)
            val successResponse = response as RepositoryResult.Success
            assertEquals(successResponse.result?.isRecordSaved, true)
        }
    }

    @Test
    fun saveCallRecordWithApiError() {
        runBlockingTest {
            whenever(callMonitorRemoteDataSource.saveCallRecord(SavedLogRecordStatus(callStatusMock,
                callRecordMock)))
                .thenReturn(NetworkResponse.ApiError(SavedRecordStatus(status = 405,
                    message = "error",
                    isRecordSaved = false), 405))
            whenever(callMonitorApi.saveCallRecord("test_url",
                SavedLogRecordStatus(callStatusMock, callRecordMock)))
                .thenReturn(NetworkResponse.ApiError(SavedRecordStatus(status = 405,
                    message = "error",
                    isRecordSaved = false), 405))
            val response = callMonitorRepository.saveCallRecord(callStatusMock, callRecordMock)
            assertNotNull(response)
            assert(response is RepositoryResult.Fail<*>)
        }
    }

    @Test
    fun saveCallRecordNetworkError() {
        runBlockingTest {
            whenever(callMonitorRemoteDataSource.saveCallRecord(SavedLogRecordStatus(callStatusMock,
                callRecordMock)))
                .thenReturn(NetworkResponse.NetworkError(IOException()))
            whenever(callMonitorApi.saveCallRecord("test_url",
                SavedLogRecordStatus(callStatusMock, callRecordMock)))
                .thenReturn(NetworkResponse.NetworkError(IOException()))
            val response = callMonitorRepository.saveCallRecord(callStatusMock, callRecordMock)
            assertNotNull(response)
            assert(response is RepositoryResult.Exception)
        }
    }

    @Test
    fun getCallLogListSuccess() {
        runBlockingTest {
            val callLogList = listOf(
                CallRecordResponse(
                    "Pepe", "12345678", 3, "DD-MM-YY", "56"
                )
            )

            whenever(callMonitorRemoteDataSource.getCallRecordList())
                .thenReturn(NetworkResponse.Success(callLogList))

            whenever(callMonitorApi.getCallRecordList("url_test"))
                .thenReturn(NetworkResponse.Success(callLogList))
            val response = callMonitorRepository.getCallRecordList()
            assertNotNull(response)
            assert(response is RepositoryResult.Success)
        }
    }

    @Test
    fun getCallLogListWithApiError() {
        runBlockingTest {
            whenever(callMonitorRemoteDataSource.getCallRecordList())
                .thenReturn(NetworkResponse.ApiError(listOf(), 405))

            whenever(callMonitorApi.getCallRecordList("url_test"))
                .thenReturn(NetworkResponse.ApiError(listOf(), 405))
            val response = callMonitorRepository.getCallRecordList()
            assertNotNull(response)
            assert(response is RepositoryResult.Fail<*>)
        }
    }

    @Test
    fun getCallLogListWithNetworkError() {
        runBlockingTest {
            whenever(callMonitorRemoteDataSource.getCallRecordList())
                .thenReturn(NetworkResponse.NetworkError(IOException()))

            whenever(callMonitorApi.getCallRecordList("url_test"))
                .thenReturn(NetworkResponse.NetworkError(IOException()))
            val response = callMonitorRepository.getCallRecordList()
            assertNotNull(response)
            assert(response is RepositoryResult.Exception)
        }
    }
}
