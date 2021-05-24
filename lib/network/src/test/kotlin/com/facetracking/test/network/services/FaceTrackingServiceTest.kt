package com.facetracking.test.network.services

import com.facetracking.network.model.mapper.toRepositoryResult
import com.facetracking.network.service.FaceTrackingService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import okhttp3.MultipartBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import com.nhaarman.mockitokotlin2.mock
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

object MockResponses {
    object GetCatalogInfo {
        const val STATUS_200 = "mock-responses/save-video-status200.json"
        const val STATUS_404 = "mock-responses/save-video-status404.json"
    }
}

@ExperimentalCoroutinesApi
class FaceTrackingServiceTest {

    private val dispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(dispatcher)

    private lateinit var faceTrackingService: FaceTrackingService
    private lateinit var mockWebServer: MockWebServer
    private val file: File = mock()
    private val body = MultipartBody.Part.createFormData("uploaded_file",
            file.name,mock())

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        faceTrackingService = Retrofit.Builder()
            .baseUrl(mockWebServer.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FaceTrackingService::class.java)
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
    fun responseSaveVideoCode200() {
        enqueueResponse(MockResponses.GetCatalogInfo.STATUS_200)
        testScope.runBlockingTest {
            val job = launch {
                val response = faceTrackingService.saveVideo(body)
                assertEquals(
                    true,
                    response.toRepositoryResult { it.isVideoSaved }
                )
            }
            job.cancel()
        }
    }

    @Test
    fun responseSaveVideoCode400() {
        enqueueResponse(MockResponses.GetCatalogInfo.STATUS_404)
        testScope.runBlockingTest {
            val job = launch {
                val response = faceTrackingService.saveVideo(body)
                assertEquals(400, response)
                assertEquals(false, response.toRepositoryResult{it.isVideoSaved})
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
