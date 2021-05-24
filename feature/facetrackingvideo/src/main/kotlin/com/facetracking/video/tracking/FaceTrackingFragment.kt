package com.facetracking.video.tracking

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.camera2.CameraMetadata.LENS_FACING_BACK
import android.hardware.camera2.CameraMetadata.LENS_FACING_FRONT
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.View.GONE
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.facetracking.video.model.UIState
import com.facetracking.video.R
import com.facetracking.video.model.ProgressRequestBody
import com.facetracking.video.view.FaceTrackingView
import com.facetracking.video.view.FaceTrackingView.Companion.VIDEO_RECORD_USE_CASE
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_facetracking_video.allow
import kotlinx.android.synthetic.main.fragment_facetracking_video.permission_message
import kotlinx.android.synthetic.main.fragment_facetracking_video.permission_title
import kotlinx.android.synthetic.main.fragment_facetracking_video.permission_layout
import kotlinx.android.synthetic.main.fragment_facetracking_video.permission_icon
import kotlinx.android.synthetic.main.fragment_facetracking_video.face_tracking_view
import kotlinx.android.synthetic.main.fragment_facetracking_video.openFrontCamera
import kotlinx.android.synthetic.main.fragment_facetracking_video.loading_bar
import kotlinx.android.synthetic.main.fragment_facetracking_video.text_view_status
import kotlinx.coroutines.flow.collect
import java.io.File

@AndroidEntryPoint
class FaceTrackingFragment : Fragment(), FaceTrackingView.FaceTrackerListener,
        ProgressRequestBody.UploadCallbacks {

    private val viewModel: FaceTrackingViewModel by viewModels()
    private lateinit var checkLocationPermission: ActivityResultLauncher<Array<String>>
    private var lensFacing = LENS_FACING_BACK

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_facetracking_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
        checkForPermissions()
        setStatesListener()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLocationPermission = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[Manifest.permission.CAMERA] == true &&
                    permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true &&
                    permissions[Manifest.permission.RECORD_AUDIO] == true
            ) {
                setupCamera()
            } else {
                showPermissionMessage()
            }
        }
    }

    override fun onFaceDetected(isFaceValid: Boolean) {
        face_tracking_view.start(
                this@FaceTrackingFragment,
                this@FaceTrackingFragment,
                lensFacing,
                VIDEO_RECORD_USE_CASE
        )
    }

    override fun onVideoRecordUseCaseSet(shouldContinue: Boolean) {
        face_tracking_view.startRecording(
                File(
                        this.activity?.externalMediaDirs?.first(),
                        "${System.currentTimeMillis()}.mp4"
                )
        )

        Handler(Looper.getMainLooper()).postDelayed({
            face_tracking_view.stopRecording()
        }, 3000)
    }

    override fun videoSavedSuccessfully(file: File) {
        uploadVideo(file)
    }

    override fun onVideoStatus(message: String) {
        view?.let {
            val snackBar = Snackbar.make(
                    it,
                    message,
                    Snackbar.LENGTH_LONG
            )
            snackBar.show()
        }
    }

    private fun setStatesListener() {
        lifecycleScope.launchWhenStarted {
            viewModel.viewStateFlow.collect { faceTrackingState ->
                when (faceTrackingState.uiState) {
                    UIState.LOADING -> {
                        text_view_status.visibility = VISIBLE
                        loading_bar.visibility = VISIBLE
                    }
                    UIState.CONTENT -> clearUseCasesAndStartAgain()
                    UIState.ERROR -> onVideoStatus(getString(R.string.snack_bar_error_message))
                    UIState.IDLE -> {
                        // do nothing
                    }
                }
            }
        }
    }

    private fun uploadVideo(file: File) {
        activity?.runOnUiThread {
            face_tracking_view.visibility = GONE
            val requestFile = ProgressRequestBody(file, this@FaceTrackingFragment)
            viewModel.saveVideo(requestFile)
        }
    }

    private fun checkForPermissions() {
        if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            setupCamera()
        } else {
            checkLocationPermission.launch(
                    arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO
                    )
            )
        }
    }

    private fun setUpClickListeners() {
        allow.setOnClickListener {
            checkForPermissions()
        }

        openFrontCamera.setOnClickListener {
            face_tracking_view.stop()
            face_tracking_view.start(
                    this@FaceTrackingFragment,
                    this@FaceTrackingFragment,
                    if (lensFacing == LENS_FACING_BACK) {
                        lensFacing = LENS_FACING_FRONT
                        LENS_FACING_FRONT
                    } else {
                        lensFacing = LENS_FACING_BACK
                        LENS_FACING_BACK
                    }
            )

        }
    }

    private fun clearUseCasesAndStartAgain() {
        loading_bar.visibility = INVISIBLE
        onVideoStatus(getString(R.string.snack_bar_successful_message))
        setupCamera()
    }

    private fun setupCamera() {
        text_view_status.visibility = INVISIBLE
        permission_layout.visibility = GONE
        face_tracking_view.visibility = VISIBLE
        face_tracking_view.start(this@FaceTrackingFragment, this@FaceTrackingFragment)
    }

    private fun showPermissionMessage() {
        face_tracking_view.visibility = GONE
        permission_title.setText(R.string.face_tracking_camera_access)
        permission_message.setText(R.string.face_tracking_enable_camera_permission)
        permission_icon.setImageResource(R.drawable.vec_ic_camera_big)
        permission_layout.visibility = VISIBLE
    }

    override fun onDestroyView() {
        face_tracking_view.stop()
        super.onDestroyView()
    }

    override fun onProgressUpdate(percentage: Int) {
        text_view_status.text = "$percentage / 100"
        loading_bar.progress = percentage
    }

    override fun onError() {
        loading_bar.visibility = INVISIBLE
        onVideoStatus(getString(R.string.snack_bar_error_message))
    }

    override fun onFinish() {
        text_view_status.text = "100 / 100"
        loading_bar.progress = 100
    }
}
