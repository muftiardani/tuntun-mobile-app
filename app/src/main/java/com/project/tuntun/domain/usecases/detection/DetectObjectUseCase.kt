package com.project.tuntun.domain.usecases.detection

import android.graphics.Bitmap
import com.project.tuntun.domain.manager.objectDetection.ObjectDetectionManager
import com.project.tuntun.domain.model.Detection

class DetectObjectUseCase(
    private val objectDetectionManager: ObjectDetectionManager
) {
    fun execute(
        bitmap: Bitmap,
        rotation: Int,
        confidenceThreshold: Float
    ): List<Detection> {
        return objectDetectionManager.detectObjectsInCurrentFrame(
            bitmap = bitmap,
            rotation,
            confidenceThreshold
        )
    }
}