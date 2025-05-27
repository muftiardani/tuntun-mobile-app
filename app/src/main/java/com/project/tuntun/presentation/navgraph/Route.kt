package com.project.tuntun.presentation.navgraph

import com.project.tuntun.presentation.utils.Routes

sealed class Route(
    val route: String
) {
    object AppStartNavigation: Route(route = Routes.ROUTE_APP_START_NAVIGATION)
    object HomeNavigation: Route(route = Routes.ROUTE_HOME_NAVIGATION)

    object OnBoardingScreen: Route(route = Routes.ROUTE_ONBOARDING_SCREEN)
    object HomeScreen: Route(route = Routes.ROUTE_HOME_SCREEN)
}
