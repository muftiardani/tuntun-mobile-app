package com.project.tuntun.presentation.home

import android.graphics.RectF
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.tuntun.R
import com.project.tuntun.data.manager.objectDetection.ObjectDetectionManagerImpl
import com.project.tuntun.domain.model.Detection
import com.project.tuntun.presentation.common.ImageButton
import com.project.tuntun.presentation.home.components.CameraOverlay
import com.project.tuntun.presentation.home.components.CameraPreview
import com.project.tuntun.presentation.home.components.ObjectCounter
import com.project.tuntun.presentation.home.components.RequestPermissions
import com.project.tuntun.presentation.home.components.ThresholdLevelSlider
import com.project.tuntun.presentation.utils.Dimens
import com.project.tuntun.utils.CameraFrameAnalyzer
import com.project.tuntun.utils.Constants
import com.project.tuntun.utils.ImageScalingUtils

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val viewModel: HomeViewModel = hiltViewModel()

    RequestPermissions()

    val isImageSavedStateFlow by viewModel.isImageSavedStateFlow.collectAsState()

    val previewSizeState = remember { mutableStateOf(IntSize(0, 0)) }

    val boundingBoxCoordinatesState = remember { mutableStateListOf<RectF>() }

    val confidenceScoreState = remember { mutableFloatStateOf(Constants.INITIAL_CONFIDENCE_SCORE) }

    var scaleFactorX = 1f
    var scaleFactorY = 1f

    val screenWidth = LocalContext.current.resources.displayMetrics.widthPixels * 1f
    val screenHeight = LocalContext.current.resources.displayMetrics.heightPixels * 1f

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        var detections by remember {
            mutableStateOf(emptyList<Detection>())
        }

        LaunchedEffect(detections) {}

        val cameraFrameAnalyzer =  remember {
            CameraFrameAnalyzer(
                objectDetectionManager = ObjectDetectionManagerImpl(
                    context = context
                ),
                onObjectDetectionResults = {
                    detections = it

                    boundingBoxCoordinatesState.clear()
                    detections.forEach { detection ->
                        boundingBoxCoordinatesState.add(detection.boundingBox)
                    }
                },
                confidenceScoreState = confidenceScoreState
            )
        }

        val cameraController = remember {
            viewModel.prepareCameraController(
                context,
                cameraFrameAnalyzer
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.gray_900)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
            ) {
                CameraPreview(
                    controller =  remember {
                        cameraController
                    },
                    modifier = Modifier.fillMaxSize(),
                    onPreviewSizeChanged = { newSize ->
                        previewSizeState.value = newSize

                        val scaleFactors = ImageScalingUtils.getScaleFactors(
                            newSize.width,
                            newSize.height
                        )

                        scaleFactorX = scaleFactors[0]
                        scaleFactorY = scaleFactors[1]

                        Log.d("HomeViewModel", "HomeScreen() called with: newSize = $scaleFactorX & scaleFactorY = $scaleFactorY")
                    }
                )

                CameraOverlay(detections = detections)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f)
                    .padding(top = Dimens.Padding8dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                ImageButton(
                    drawableResourceId = R.drawable.ic_capture,
                    contentDescriptionResourceId = R.string.capture_button_description,
                    modifier = Modifier
                        .size(Dimens.CaptureButtonSize)
                        .clip(CircleShape)
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            viewModel.capturePhoto(
                                context = context,
                                cameraController = cameraController,
                                screenWidth,
                                screenHeight,
                                detections
                            )

                            if (isImageSavedStateFlow) {
                                Toast
                                    .makeText(
                                        context,
                                        R.string.success_image_saved_message,
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            } else {
                                Toast
                                    .makeText(
                                        context,
                                        R.string.error_image_saved_message,
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        }
                )

                val sliderValue = remember { mutableFloatStateOf(Constants.INITIAL_CONFIDENCE_SCORE) }
                ThresholdLevelSlider(sliderValue) { sliderValue ->
                    confidenceScoreState.floatValue = sliderValue
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f)
                .padding(top = Dimens.Padding32dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                ImageButton(
                    drawableResourceId = R.drawable.ic_rotate_camera,
                    contentDescriptionResourceId = R.string.rotate_camera_button_description,
                    Modifier
                        .padding(
                            top = Dimens.Padding24dp,
                            start = Dimens.Padding16dp
                        )
                        .size(Dimens.RotateCameraButtonSize)
                        .clickable {
                            cameraController.cameraSelector =
                                viewModel.getSelectedCamera(cameraController)
                        }
                )

                ObjectCounter(objectCount = detections.size)
            }
        }
    }
}