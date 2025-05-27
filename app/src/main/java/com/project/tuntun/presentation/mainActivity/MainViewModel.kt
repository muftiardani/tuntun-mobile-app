package com.project.tuntun.presentation.mainActivity

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.tuntun.domain.usecases.userconfig.UserConfigUseCases
import com.project.tuntun.presentation.navgraph.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userConfigUseCases: UserConfigUseCases
): ViewModel() {
    companion object {
        private val TAG: String? = MainViewModel::class.simpleName
    }

    var redirectFlagState by mutableStateOf(true)

    var startDestination by mutableStateOf(Route.AppStartNavigation.route)

    init {
        userConfigUseCases.readUserConfig().onEach { shouldStartFromHomeScreen ->
            Log.d(TAG, "init() called with shouldStartFromHomeScreen flag = $shouldStartFromHomeScreen")

            startDestination = if (shouldStartFromHomeScreen) {
                Route.HomeNavigation.route
            } else {
                Route.AppStartNavigation.route
            }

            delay(400)
            redirectFlagState = false
        }.launchIn(viewModelScope)
    }
}