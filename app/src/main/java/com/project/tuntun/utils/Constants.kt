package com.project.tuntun.utils

object Constants {
    const val USER_CONFIG_DATASTORE_NAME = "userConfigDatastore"
    const val USER_CONFIG = "userConfig"

    const val INITIAL_CONFIDENCE_SCORE = 0.5f
    const val MODEL_INPUT_IMAGE_WIDTH = 384
    const val MODEL_INPUT_IMAGE_HEIGHT = 384
    const val ORIGINAL_IMAGE_WIDTH = 480f
    const val ORIGINAL_IMAGE_HEIGHT = 640f

    const val MODEL_MAX_RESULTS_COUNT = 10
    const val MODEL_PATH = "efficientdet-lite1.tflite"
}