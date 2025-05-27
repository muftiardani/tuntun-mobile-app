package com.project.tuntun.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.tuntun.domain.usecases.userconfig.UserConfigUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val userConfigUseCases: UserConfigUseCases
): ViewModel() {

    fun onEvent(event: OnBoardingEvent) {
        when(event) {
            is OnBoardingEvent.WriteUserConfigToDataStore -> {
                writeUserConfig()
            }
        }
    }

    private fun writeUserConfig() {
        viewModelScope.launch {
            userConfigUseCases.writeUserConfig()
        }
    }
}