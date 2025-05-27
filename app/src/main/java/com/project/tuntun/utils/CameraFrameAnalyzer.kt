package com.project.tuntun.utils

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.State
import com.project.tuntun.domain.manager.objectDetection.ObjectDetectionManager
import com.project.tuntun.domain.model.Detection
import javax.inject.Inject

class CameraFrameAnalyzer @Inject constructor(
    private val objectDetectionManager: ObjectDetectionManager,
    private val onObjectDetectionResults: (List<Detection>) -> Unit,
    private val confidenceScoreState: State<Float>
): ImageAnalysis.Analyzer {
    private var frameSkipCounter = 0

    override fun analyze(image: ImageProxy) {
        if (frameSkipCounter % 60 == 0) {
            val rotatedImageMatrix: Matrix =
                Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                }

            val rotatedBitmap: Bitmap = Bitmap.createBitmap(
                image.toBitmap(),
                0,
                0,
                image.width,
                image.height,
                rotatedImageMatrix,
                true
            )

            val objectDetectionResults = objectDetectionManager.detectObjectsInCurrentFrame(
                bitmap = rotatedBitmap,
                image.imageInfo.rotationDegrees,
                confidenceThreshold = confidenceScoreState.value
            )
            onObjectDetectionResults(objectDetectionResults)
        }
        frameSkipCounter++

        image.close()
    }
}