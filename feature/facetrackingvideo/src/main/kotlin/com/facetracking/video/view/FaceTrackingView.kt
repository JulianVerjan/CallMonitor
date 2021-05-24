package com.facetracking.video.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.VideoCapture
import androidx.camera.core.Preview
import androidx.camera.core.impl.VideoCaptureConfig
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.facetracking.video.databinding.ViewScannerBinding
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.io.File
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@SuppressLint("RestrictedApi")
class FaceTrackingView : PreviewView, LifecycleOwner {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .build()
    private val detector = FaceDetection.getClient(options)

    private val registry: LifecycleRegistry = LifecycleRegistry(this)
    private val executor = Executors.newSingleThreadExecutor()
    private var analysisUseCase: ImageAnalysis? = null
    private var videoRecordUseCase: VideoCapture? = null

    private lateinit var cameraProvider: ProcessCameraProvider
    private var preview: Preview? = null
    private var faceTrackerListener: FaceTrackerListener? = null

    private var _binding: ViewScannerBinding? = null
    private val binding get() = _binding!!

    init {
        _binding = ViewScannerBinding.inflate(LayoutInflater.from(context), this)
    }

    internal fun start(
        lifecycleOwner: LifecycleOwner, faceTrackerListener: FaceTrackerListener?,
        lensFacingType: Int = CameraSelector.LENS_FACING_BACK,
        useCaseType: Int = FACE_DETECTION_USE_CASE
    ) {
        this.faceTrackerListener = faceTrackerListener
        binding.cameraView.post {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener(
                {
                    cameraProvider = cameraProviderFuture.get()
                    if (useCaseType == FACE_DETECTION_USE_CASE)
                        bindFaceDetectionUseCase(lifecycleOwner, lensFacingType)
                    else bindVideoRecordUseCase(lifecycleOwner, lensFacingType)

                },
                ContextCompat.getMainExecutor(context)
            )
        }
    }

    internal fun stop() {
        if (::cameraProvider.isInitialized) {
            cameraProvider.unbindAll()
        }
    }

    @SuppressLint("WrongConstant")
    private fun bindFaceDetectionUseCase(
        lifecycleOwner: LifecycleOwner,
        lensFacingType: Int
    ) {
        val metrics = DisplayMetrics().also { binding.cameraView.display.getRealMetrics(it) }
        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        val rotation = binding.cameraView.display.rotation
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacingType)
            .build()

        preview = Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()

        val faceContainerBox = Rect()
        faceContainerBox.bottom = binding.faceSquareBox.bottom
        faceContainerBox.top = binding.faceSquareBox.top
        faceContainerBox.left = binding.faceSquareBox.left
        faceContainerBox.right = binding.faceSquareBox.right
        val analyzer = FaceTrackingImageAnalyzer(
            scanner = detector,
            faceContainerBox = faceContainerBox
        ) { face ->
            if(face.isEmpty().not()) {
                faceTrackerListener?.onFaceDetected(true)
                cameraProvider.unbindAll()
            }
        }

        analysisUseCase = ImageAnalysis.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .setCameraSelector(cameraSelector)
            .build()

        analysisUseCase?.setAnalyzer(executor, analyzer)
        cameraProvider.unbindAll()

        try {
            cameraProvider.bindToLifecycle(
                lifecycleOwner, cameraSelector, preview, analysisUseCase
            )
            preview?.setSurfaceProvider(binding.cameraView.createSurfaceProvider())
        } catch (exc: Exception) {
            Log.e("CameraView", "Failed to bind camera provider to lifecycle")
        }
    }

    @SuppressLint("WrongConstant")
    private fun bindVideoRecordUseCase(
        lifecycleOwner: LifecycleOwner,
        lensFacingType: Int
    ) {
        val metrics = DisplayMetrics().also { binding.cameraView.display.getRealMetrics(it) }
        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        val rotation = binding.cameraView.display.rotation
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacingType)
            .build()

        preview = Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()

        val videoCaptureConfig = VideoCaptureConfig.Builder()
            .setTargetRotation(rotation)
            .setCameraSelector(cameraSelector)

        videoRecordUseCase = VideoCapture(videoCaptureConfig.useCaseConfig)
        cameraProvider.unbindAll()

        try {
            cameraProvider.bindToLifecycle(
                lifecycleOwner, cameraSelector, preview, videoRecordUseCase
            )
            preview?.setSurfaceProvider(binding.cameraView.createSurfaceProvider())
            faceTrackerListener?.onVideoRecordUseCaseSet(true)
        } catch (exc: Exception) {
            Log.e("CameraView", "Failed to bind camera provider to lifecycle")
            faceTrackerListener?.onVideoRecordUseCaseSet(false)
        }
    }

    /**
     * Taken from Android Sample app
     * https://github.com/android/camera-samples/blob/master/CameraXBasic/app/src/main/java/com/android/example/cameraxbasic/fragments/CameraFragment.kt
     */
    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        return if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            AspectRatio.RATIO_4_3
        } else {
            AspectRatio.RATIO_16_9
        }
    }

    fun startRecording(file: File) {
        videoRecordUseCase?.startRecording(
            file,
            executor,
            object : VideoCapture.OnVideoSavedCallback {
                override fun onVideoSaved(file: File) {
                    faceTrackerListener?.videoSavedSuccessfully(file)
                }

                override fun onError(videoCaptureError: Int, message: String, cause: Throwable?) {
                    faceTrackerListener?.onVideoStatus(message)
                }
            })
    }

    fun stopRecording() {
        videoRecordUseCase?.stopRecording()
    }

    override fun getLifecycle(): Lifecycle = registry

    companion object {
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
        const val FACE_DETECTION_USE_CASE = 1
        const val VIDEO_RECORD_USE_CASE = 2
    }

    interface FaceTrackerListener {
        fun onFaceDetected(isFaceValid: Boolean)
        fun onVideoRecordUseCaseSet(shouldContinue: Boolean)
        fun videoSavedSuccessfully(file: File)
        fun onVideoStatus(message: String)
    }
}
