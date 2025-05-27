package com.project.tuntun.presentation.onboarding

sealed class OnBoardingEvent {
    object WriteUserConfigToDataStore: OnBoardingEvent()
}