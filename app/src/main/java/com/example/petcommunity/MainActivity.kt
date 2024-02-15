package com.example.petcommunity

import android.app.Activity
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcommunity.ui.theme.PetCommunityTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.petcommunity.data.local_data.DataStoreManager
import com.example.petcommunity.presentation.RootNavigation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("XXXX", "viewModel")

        setContent {
            PetCommunityTheme {
                val rootNavController = rememberNavController()
                val screen by viewModel.startDestination

                RootNavigation(rootNavController, viewModel.startDestination.value)
            }
        }

    }
}

class MyViewModel @Inject constructor(dataStoreManager: DataStoreManager) : ViewModel() {
    private val _startDestination: MutableState<String> = mutableStateOf("onboarding")
    val startDestination: State<String> = _startDestination

    init {
        viewModelScope.launch(Dispatchers.Main) {
            dataStoreManager.getRunFirst().collect { completed ->

                if (completed) {
                    _startDestination.value = "onboarding"
                } else {
                    _startDestination.value = "login"

                }
                Log.d("XXX", completed.toString())
            }
        }
    }
}

@Composable
fun Test() {
    val testViewModel = hiltViewModel<TestViewModel>()
    val uiState = testViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val activity = LocalContext.current as Activity


    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(),
            onResult = {

                if (it) {
                    Log.d("XXXX", "oke")
                } else {
                    Log.d("XXXX", "not oke")

                }
            })

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = {

//            launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val builder = NotificationCompat.Builder(context, "channel 1")
                .setSmallIcon(R.drawable.corgi_hello_logo).setContentTitle("Hello")
                .setContentText("Hello word").setStyle(
                    NotificationCompat.BigTextStyle().bigText("AAAA")
                ).setContentIntent(pendingIntent)

                .setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_DEFAULT).build()


            notificationManager.notify(1, builder)

//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                val name = "channel1"
//                val descriptionText = "descriptionText channel1"
//                val importance = NotificationManager.IMPORTANCE_DEFAULT
//                val channel = NotificationChannel("channel 1", name, importance).apply {
//                    description = descriptionText
//                }
//                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//                notificationManager.createNotificationChannel(channel)
//
//            }


        }) {}
    }) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Text(text = uiState.value.toString())

        }
    }
}

@HiltViewModel
class TestViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(0)
    val uiState = _uiState.asStateFlow()

    fun test() {
        viewModelScope.launch {
            _uiState.emit(_uiState.value + 1)
        }
    }
}

