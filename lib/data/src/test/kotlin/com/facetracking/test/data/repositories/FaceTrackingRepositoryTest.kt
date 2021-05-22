package com.facetracking.test.data.repositories

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.facetracking.data.datasource.FaceTrackingRemoteDataSource
import com.facetracking.data.repository.FaceTrackingRepository
import com.facetracking.network.model.NetworkResponse
import com.facetracking.network.model.mapper.RepositoryResult
import com.facetracking.network.model.reponse.SaveVideoNetworkResponse
import com.facetracking.network.service.FaceTrackingService
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
class FaceTrackingRepositoryTest {

    @MockK(relaxed = true)
    lateinit var faceTrackingService: FaceTrackingService
    private lateinit var faceTrackingRepository: FaceTrackingRepository
    private lateinit var faceTrackingRemoteDataSource: FaceTrackingRemoteDataSource
    private val mockData = SaveVideoNetworkResponse(
        status = 200,
        message = "Video saved",
        isVideoSaved = true
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        faceTrackingService = mock()
        faceTrackingRemoteDataSource = FaceTrackingRemoteDataSource(faceTrackingService)
        faceTrackingRepository = FaceTrackingRepository(faceTrackingRemoteDataSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun saveVideoSuccessfully() {
        runBlockingTest {

            whenever(faceTrackingService.saveVideo())
                .thenReturn(NetworkResponse.Success(mockData))

            val response = faceTrackingRepository.saveVideo()
            assert(response is RepositoryResult.Success)
            val successResponse = response as RepositoryResult.Success
            assertEquals(successResponse.result?.isVideoSaved, true)
        }
    }

    @Test
    fun saveVideoWithApiError() {
        runBlockingTest {
            whenever(faceTrackingService.saveVideo())
                .thenReturn(NetworkResponse.ApiError("Error", 400))
            val response = faceTrackingRepository.saveVideo()
            assertNotNull(response)
            assert(response is RepositoryResult.Fail<*>)
        }
    }

    @Test
    fun saveVideoWithNetworkError() {
        runBlockingTest {
            whenever(faceTrackingService.saveVideo())
                .thenReturn(NetworkResponse.NetworkError(IOException()))
            val response = faceTrackingRepository.saveVideo()
            assertNotNull(response)
            assert(response is RepositoryResult.Exception)
        }
    }
}
