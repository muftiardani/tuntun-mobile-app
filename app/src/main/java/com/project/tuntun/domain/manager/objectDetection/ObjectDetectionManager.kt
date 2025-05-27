package com.project.tuntun.domain.manager.objectDetection

import android.graphics.Bitmap
import com.project.tuntun.domain.model.Detection

interface ObjectDetectionManager {
    fun detectObjectsInCurrentFrame(
        bitmap: Bitmap,
        rotation: Int,
        confidenceThreshold: Float
    ): List<Detection>
}