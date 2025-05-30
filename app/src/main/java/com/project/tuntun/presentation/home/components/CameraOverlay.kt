package com.project.tuntun.presentation.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.project.tuntun.domain.model.Detection
import com.project.tuntun.presentation.common.DrawDetectionBox

@Composable
fun CameraOverlay(detections: List<Detection>) {
    Box(modifier = Modifier.fillMaxSize()) {
        detections.forEach { detection ->
            DrawDetectionBox(detection)
        }
    }
}