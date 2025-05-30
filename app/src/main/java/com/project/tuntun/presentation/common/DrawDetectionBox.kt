package com.project.tuntun.presentation.common

import android.graphics.Paint
import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.project.tuntun.domain.model.Detection
import kotlin.random.Random

@Composable
fun DrawDetectionBox(detection: Detection) {
    val screenWidth = LocalContext.current.resources.displayMetrics.widthPixels * 1f
    val screenHeight = LocalContext.current.resources.displayMetrics.heightPixels * 1f

    val paint = rememberUpdatedState(Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 8f
        color = getColorForLabel(detection.detectedObjectName)
    })

    val xScale = screenWidth / detection.tensorImageWidth
    val yScale = screenHeight / detection.tensorImageHeight

    val scaledBox = RectF(
        detection.boundingBox.left * xScale,
        detection.boundingBox.top * yScale,
        detection.boundingBox.right * xScale,
        detection.boundingBox.bottom * yScale
    ).also {
        it.left = it.left.coerceAtLeast(0f)
        it.top = it.top.coerceAtLeast(0f)
        it.right = it.right.coerceAtMost(screenWidth)
        it.bottom = it.bottom.coerceAtMost(screenHeight)
    }

    val androidColor = android.graphics.Color.argb(
        (paint.value.color.alpha * 255),
        (paint.value.color.red * 255),
        (paint.value.color.green * 255),
        (paint.value.color.blue * 255)
    )

    val density = LocalDensity.current.density
    val desiredTextSizeInSp = 20
    val pixelSize = desiredTextSizeInSp * density

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(
            modifier = Modifier.matchParentSize(),
            onDraw = {
                drawRect(
                    color = Color(paint.value.color),
                    size = Size(scaledBox.width(), scaledBox.height()),
                    topLeft = Offset(scaledBox.left, scaledBox.top),
                    style = Stroke(paint.value.strokeWidth)
                )

                val text =
                    "${detection.detectedObjectName} ${(detection.confidenceScore * 100).toInt()}%"
                drawIntoCanvas { canvas ->
                    canvas.nativeCanvas.drawText(
                        text,
                        scaledBox.left,
                        scaledBox.top - 10,
                        Paint().apply {
                            color = androidColor
                            textSize = pixelSize
                        }
                    )
                }
            }
        )
    }
}

private val labelColorMap = mutableMapOf<String, Int>()

fun getColorForLabel(label: String): Int {
    return labelColorMap.getOrPut(label) {
        Random.nextInt()
    }
}