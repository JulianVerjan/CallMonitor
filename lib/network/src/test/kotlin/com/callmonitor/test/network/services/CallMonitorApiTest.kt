package com.callmonitor.test.network.services

import com.callmonitor.model.CallStatus
import com.callmonitor.network.model.mapper.toRepositoryResult
import com.callmonitor.network.service.CallMonitorApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MockResponses {
    object GetCatalogInfo {
        const val STATUS_200 = "mock-responses/save-call-status-status200.json"
        const val CALL_LOG_STATUS_200 = "mock-responses/get-call-log-status200.json"
        const val CALL_SERVICES_LIST_STATUS_200 = "mock-responses/get-services-list-status200.json"
        const val STATUS_404 = "mock-responses/save-status-status404.json"
    }
}

@ExperimentalCoroutinesApi
class CallMonitorApiTest {

    private val dispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(dispatcher)

    private lateinit var callMonitorApi: CallMonitorApi
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        callMonitorApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CallMonitorApi::class.java)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        Dispatchers.resetMain()
        // Clean up the TestCoroutineDispatcher to make sure no other work is running.
        dispatcher.cleanupTestCoroutines()
        testScope.cleanupTestCoroutines()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun responseSaveStatusCode200() {
        enqueueResponse(MockResponses.GetCatalogInfo.STATUS_200)
        testScope.runBlockingTest {
            val job = launch {
                val response =
                    callMonitorApi.saveCallStatus("url_test", CallStatus("Test", "12345678", true))
                assertEquals(
                    true,
                    response.toRepositoryResult { it.isRecordSaved }
                )
            }
            job.cancel()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun responseGetCallLogListCode200() {
        enqueueResponse(MockResponses.GetCatalogInfo.CALL_LOG_STATUS_200)
        testScope.runBlockingTest {
            val job = launch {
                val response =
                    callMonitorApi.getCallRecordList("url_test")
                assertEquals(
                    true,
                    response.toRepositoryResult { it }
                )
            }
            job.cancel()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun responseGetServicesListCode200() {
        enqueueResponse(MockResponses.GetCatalogInfo.CALL_SERVICES_LIST_STATUS_200)
        testScope.runBlockingTest {
            val job = launch {
                val response =
                    callMonitorApi.getCallServicesInfo("url_test")
                assertEquals(
                    true,
                    response.toRepositoryResult { it }
                )
            }
            job.cancel()
        }
    }

    @Test
    fun responseSaveCallStatusCode400() {
        enqueueResponse(MockResponses.GetCatalogInfo.STATUS_404)
        testScope.runBlockingTest {
            val job = launch {
                val response =
                    callMonitorApi.saveCallStatus("url_test", CallStatus("Test", "12345678", true))
                assertEquals(400, response)
                assertEquals(false, response.toRepositoryResult { it.isRecordSaved })
                assertNull(response)
            }
            job.cancel()
        }
    }

    private fun enqueueResponse(filePath: String) {
        val inputStream = javaClass.classLoader?.getResourceAsStream(filePath)
        val bufferSource = inputStream?.source()?.buffer()
        val mockResponse = MockResponse()

        mockWebServer.enqueue(
            mockResponse.setBody(
                bufferSource!!.readString(Charsets.UTF_8)
            )
        )
    }
}
