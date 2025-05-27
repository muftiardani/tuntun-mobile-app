package com.project.tuntun.data.manager.objectDetection

import android.content.Context
import android.graphics.Bitmap
import android.view.Surface
import com.project.tuntun.domain.manager.objectDetection.ObjectDetectionManager
import com.project.tuntun.domain.model.Detection
import com.project.tuntun.utils.Constants
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.detector.ObjectDetector
import javax.inject.Inject

class ObjectDetectionManagerImpl @Inject constructor(
    private val context: Context
): ObjectDetectionManager {
    private var detector: ObjectDetector? = null

    override fun detectObjectsInCurrentFrame(
        bitmap: Bitmap,
        rotation: Int,
        confidenceThreshold: Float
    ): List<Detection> {
        if (detector == null) {
            initializeDetector(confidenceThreshold)
        }

        val imageProcessor =
            ImageProcessor.Builder()
                .build()

        val tensorImage: TensorImage = imageProcessor.process(
            TensorImage.fromBitmap(bitmap)
        )

        val detectionResults = detector?.detect(
            tensorImage
        )

        return detectionResults?.mapNotNull { detectedObject ->
            if ((detectedObject.categories.firstOrNull()?.score ?: 0f) >= confidenceThreshold) {
                Detection(
                    boundingBox = detectedObject.boundingBox,
                    detectedObjectName = detectedObject.categories.firstOrNull()?.label ?: "",
                    confidenceScore = detectedObject.categories.firstOrNull()?.score ?: 0f,
                    tensorImage.height,
                    tensorImage.width
                )
            } else null
        }?.take(Constants.MODEL_MAX_RESULTS_COUNT) ?: emptyList()
    }

    private fun initializeDetector(confidenceThreshold: Float) {
        try {
            val baseOptions = BaseOptions.builder()
                .setNumThreads(2)

            if (CompatibilityList().isDelegateSupportedOnThisDevice) {
                baseOptions.useGpu()
            }

            val options = ObjectDetector.ObjectDetectorOptions.builder()
                .setBaseOptions(baseOptions.build())
                .setMaxResults(Constants.MODEL_MAX_RESULTS_COUNT)
                .setScoreThreshold(confidenceThreshold)
                .build()

            detector = ObjectDetector.createFromFileAndOptions(
                context,
                Constants.MODEL_PATH,
                options
            )
        } catch (exception: IllegalStateException) {
            exception.printStackTrace()
        }
    }

    private fun initializeImageProcessingOptions(rotation: Int): ImageProcessingOptions {
        return ImageProcessingOptions.builder()
            .setOrientation(getOrientationFromRotation(rotation))
            .build()
    }

    private fun getOrientationFromRotation(rotation: Int): ImageProcessingOptions.Orientation {
        return when(rotation) {
            Surface.ROTATION_270 -> ImageProcessingOptions.Orientation.BOTTOM_RIGHT
            Surface.ROTATION_90 -> ImageProcessingOptions.Orientation.TOP_LEFT
            Surface.ROTATION_180 -> ImageProcessingOptions.Orientation.RIGHT_BOTTOM
            else -> ImageProcessingOptions.Orientation.RIGHT_TOP
        }
    }
}