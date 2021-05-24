package com.facetracking.video.view

import android.annotation.SuppressLint
import android.graphics.Rect
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetector

class FaceTrackingImageAnalyzer(
        private val scanner: FaceDetector,
        private val faceContainerBox: Rect,
        private val onFaceDetected: (face: List<Face>) -> Unit
) : ImageAnalysis.Analyzer {

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image?.let { image ->
            val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
            scanner.process(inputImage)
                    .addOnSuccessListener { faces ->
                        if (faces.isNotEmpty()) {
                            val face = faces.firstOrNull()
                            val bounding = face?.boundingBox
                            bounding?.let { imageBounding ->
                                if (imageBounding.left < faceContainerBox.left &&
                                        imageBounding.top < faceContainerBox.top &&
                                        imageBounding.bottom < faceContainerBox.bottom &&
                                        imageBounding.right < faceContainerBox.right) {
                                    onFaceDetected.invoke(faces)
                                }
                            }
                        }
                    }
                    .addOnFailureListener {
                        // do nothing
                    }.addOnCompleteListener {
                        imageProxy.close()
                    }
        }
    }
}
