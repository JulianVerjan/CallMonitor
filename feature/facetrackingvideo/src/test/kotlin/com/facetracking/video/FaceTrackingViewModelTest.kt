package com.facetracking.video

import com.facetracking.data.usecase.SaveVideoUseCase
import com.facetracking.model.SaveVideoResult
import com.facetracking.network.model.mapper.RepositoryResult
import com.facetracking.network.service.FaceTrackingService
import com.facetracking.video.model.ProgressRequestBody
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.facetracking.video.tracking.FaceTrackingViewModel
import com.facetracking.video.model.UIState
import io.mockk.MockKAnnotations
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MultipartBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File

@ExperimentalCoroutinesApi
class FaceTrackingViewModelTest {

    private val dispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(dispatcher)

    private var saveVideoUseCase = mock<SaveVideoUseCase>()
    private var faceTrackingViewModel = FaceTrackingViewModel(saveVideoUseCase)
    private lateinit var faceTrackingService: FaceTrackingService

    private val file: File = mock()
    private val body = MultipartBody.Part.createFormData("uploaded_file",
        file.name,mock())
    val requestFile = ProgressRequestBody(file, mock())

    private val saveVideoMock = SaveVideoResult(
        status = 200,
        message = "saasdsad",
        isVideoSaved = true
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        faceTrackingService = mock()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        // Clean up the TestCoroutineDispatcher to make sure no other work is running.
        dispatcher.cleanupTestCoroutines()
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun saveVideoSuccessfully() {
        testScope.runBlockingTest {
            whenever(saveVideoUseCase.saveVideo(body))
                .thenReturn(RepositoryResult.Success(saveVideoMock))

            val job = launch {
                faceTrackingViewModel.saveVideo(requestFile)
                faceTrackingViewModel.viewStateFlow.collect {
                    assertNotNull(it.uiState)
                }
            }
            job.cancel()
        }
    }

    @Test
    fun fetchWalletsWithError() {
        testScope.runBlockingTest {
            whenever(saveVideoUseCase.saveVideo(body))
                .thenReturn(RepositoryResult.Fail("error"))
            val job = launch {
                faceTrackingViewModel.saveVideo(requestFile)
                faceTrackingViewModel.viewStateFlow.collect {
                    assertEquals(it.uiState, UIState.ERROR)
                }
            }
            job.cancel()
        }
    }

    @Test
    fun fetchWalletsWithApiError() {
        testScope.runBlockingTest {
            whenever(saveVideoUseCase.saveVideo(body))
                .thenReturn(RepositoryResult.Exception(Exception()))
            val job = launch {
                faceTrackingViewModel.saveVideo(requestFile)
                faceTrackingViewModel.viewStateFlow.collect {
                    assertEquals(it.uiState, UIState.ERROR)
                }
            }
            job.cancel()
        }
    }
}
