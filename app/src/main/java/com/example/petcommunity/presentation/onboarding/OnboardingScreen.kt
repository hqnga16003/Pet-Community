package com.example.petcommunity.presentation.onboarding


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.petcommunity.R
import com.example.petcommunity.presentation.wigdet.ButtonAuth
import com.example.petcommunity.presentation.wigdet.MyTextButton
import com.example.petcommunity.presentation.wigdet.TextSubOnboarding
import com.example.petcommunity.presentation.wigdet.TextTile
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(navigateLogin: () -> Unit,navigateRegister: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = {
        items.size
    })

    val lifecycleOwner = LocalLifecycleOwner.current
    val onBoardingViewModel = hiltViewModel<OnBoardingViewModel>()
    val coroutine = rememberCoroutineScope()

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            onBoardingViewModel.onboardingEvent.collect { event ->
                when (event) {
                    is OnboardingUiEvent.NavigationRegister -> {
                        navigateRegister()
                    }

                    is OnboardingUiEvent.NavigationLogin -> {
                        navigateLogin()
                    }
                }
            }
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(
                    start = (LocalConfiguration.current.screenWidthDp * 0.025).dp,
                    end = (LocalConfiguration.current.screenWidthDp * 0.025).dp,
                    top = (LocalConfiguration.current.screenWidthDp * 0.025).dp,
                    bottom = (LocalConfiguration.current.screenWidthDp * 0.3).dp
                ), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                modifier = Modifier
                    .weight(1f), state = pagerState
            ) {
                ContentOnBoardingScreen(items[it])
            }
            if (pagerState.currentPage == 2) {
                ButtonAuth(
                    title = "REGISTER", modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = (LocalConfiguration.current.screenWidthDp * 0.1).dp)
                , onClick = {
                        onBoardingViewModel.onEvent(OnboardingUiEvent.NavigationRegister)
                    })
                Spacer(modifier = Modifier.height(10.dp))
                MyTextButton(title = "Login to continue", color = Color.Blue, onClick = {
                    onBoardingViewModel.onEvent(OnboardingUiEvent.NavigationLogin)

                })
            } else {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    MyTextButton(title = "Skip", color = Color.Blue,onClick = {
                        coroutine.launch {
                            pagerState.scrollToPage(2)
                        }
                    })

                    IconButton(onClick = {
                        coroutine.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }) {
                        Icon(Icons.Default.ArrowForward, contentDescription = "")
                    }
                }
            }
        }
    }
}


@Composable
private fun ContentOnBoardingScreen(contentOnBoarding: ContentOnBoarding) {

    Box(
        modifier = Modifier.padding((LocalConfiguration.current.screenWidthDp * 0.1).dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = contentOnBoarding.img),
                contentDescription = "",
                modifier = Modifier
                    .height((LocalConfiguration.current.screenHeightDp * 0.25).dp)
                    .width((LocalConfiguration.current.screenHeightDp * 0.25).dp)
            )
            TextTile(title = contentOnBoarding.title)
            TextSubOnboarding(title = contentOnBoarding.subTitle)
        }
    }
}

data class ContentOnBoarding(
    val title: String,
    val subTitle: String,
    val img: Int,
)

private val items = listOf(
    ContentOnBoarding(
        title = "You want to find a pet",
        subTitle = "You are a pet lover? and you want to find a pet for your house and take care it.",
        img = R.drawable.landing_page_1
    ), ContentOnBoarding(
        title = "Your pet needs a new home",
        subTitle = "You can't afford to continue taking care your pet and your " +
                "pet will need a better home an new environment right now.",
        img = R.drawable.landing_page_2
    ), ContentOnBoarding(
        title = "Welcome to Adopt Me",
        subTitle = "Adopt me is a community will help you solve all previous problem. We will support you as far as we can.",
        img = R.drawable.landing_page_3
    )

)