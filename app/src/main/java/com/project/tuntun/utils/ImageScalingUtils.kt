package com.project.tuntun.utils

import android.graphics.RectF

object ImageScalingUtils {

    fun getScaleFactors(
        targetWidth: Int,
        targetHeight: Int
    ): FloatArray {
        return try {
            val scaleX = (targetWidth.toFloat() / Constants.ORIGINAL_IMAGE_WIDTH)
            val scaleY = (targetHeight.toFloat() / Constants.ORIGINAL_IMAGE_HEIGHT)
            floatArrayOf(scaleX, scaleY)
        } catch(exception: Exception) {
            floatArrayOf(1f, 1f)
        }
    }

    fun scaleBoundingBox(
        boundingBox: RectF,
        originalWidth: Int,
        originalHeight: Int,
        cameraPreviewWidth: Int,
        cameraPreviewHeight: Int
    ): RectF {
        val scaledLeft = boundingBox.left * (cameraPreviewWidth.toFloat() / originalWidth)
        val scaledTop = boundingBox.top * (cameraPreviewHeight.toFloat() / originalHeight)
        val scaledRight = boundingBox.right * (cameraPreviewWidth.toFloat() / originalWidth)
        val scaledBottom = boundingBox.bottom * (cameraPreviewHeight.toFloat() / originalHeight)
        return RectF(scaledLeft, scaledTop, scaledRight, scaledBottom)
    }
}