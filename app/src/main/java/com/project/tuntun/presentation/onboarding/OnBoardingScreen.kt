package com.project.tuntun.presentation.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.project.tuntun.R
import com.project.tuntun.presentation.utils.Dimens
import com.project.tuntun.presentation.common.PrimaryButton
import com.project.tuntun.presentation.common.SecondaryButton
import com.project.tuntun.presentation.onboarding.components.OnBoardingPage
import com.project.tuntun.presentation.onboarding.components.OnBoardingPageIndicator
import com.project.tuntun.presentation.onboarding.model.pagesList
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(
    event: (OnBoardingEvent) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        val pagerState: PagerState = rememberPagerState(initialPage = 0) {
            pagesList.size
        }

        val buttonNext = stringResource(id = R.string.button_next)
        val buttonBack = stringResource(id = R.string.button_back)
        val buttonBegin = stringResource(id = R.string.button_begin)

        val buttonState = remember {
            derivedStateOf {
                when (pagerState.currentPage) {
                    0 -> listOf("", buttonNext)
                    1 -> listOf(buttonBack, buttonNext)
                    2 -> listOf(buttonBack, buttonBegin)
                    else -> listOf("", "")
                }
            }
        }

        HorizontalPager(state = pagerState) {pageIndex ->
            OnBoardingPage(page = pagesList[pageIndex])
        }
        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.Padding16dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OnBoardingPageIndicator(
                modifier = Modifier.width(Dimens.OnBoardingPageIndicatorWidth),
                pageCount = pagesList.size,
                selectedPage = pagerState.currentPage
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                val pagerCoroutineScope = rememberCoroutineScope()

                if (buttonState.value[0].isNotEmpty()) {
                    SecondaryButton(
                        text = buttonState.value[0],
                        onClick = {
                            pagerCoroutineScope.launch {
                                pagerState.animateScrollToPage(page = pagerState.currentPage - 1)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.padding(horizontal = Dimens.Padding8dp))
                }

                PrimaryButton(
                    text = buttonState.value[1],
                    onClick = {
                        pagerCoroutineScope.launch {
                            if (pagerState.currentPage == (pagesList.size - 1)) {
                                event(OnBoardingEvent.WriteUserConfigToDataStore)
                            } else {
                                pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
                            }
                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.weight(0.25f))
    }
}