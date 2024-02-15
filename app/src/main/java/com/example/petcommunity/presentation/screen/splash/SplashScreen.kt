package com.example.petcommunity.presentation.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SplashScreen(navigateOnboarding: () -> Unit, navigateLogin: () -> Unit,navigateHome:()->Unit) {

    val splashViewModel = hiltViewModel<SplashViewModel>()
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            splashViewModel.splashEvent.collect { isRunFist ->
                when (isRunFist) {
                    true -> {
                        if(FirebaseAuth.getInstance().currentUser==null){
                            navigateLogin()
                        }else{
                            navigateHome()
                        }
                    }
                    false -> {
                        navigateOnboarding()
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.corgi_hello_logo),
            contentDescription = "",
            modifier = Modifier
                .height((LocalConfiguration.current.screenHeightDp * 0.25).dp)
                .width((LocalConfiguration.current.screenHeightDp * 0.25).dp)
        )
    }
}

