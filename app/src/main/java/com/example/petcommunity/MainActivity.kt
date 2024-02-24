package com.example.petcommunity

import android.app.Activity
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.app.NotificationCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcommunity.ui.theme.PetCommunityTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.navigation.compose.rememberNavController
import com.example.petcommunity.data.local_data.DataStoreManager
import com.example.petcommunity.presentation.RootNavigation
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var viewModel: SplashViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseAuth.getInstance().firebaseAuthSettings.setAppVerificationDisabledForTesting(true)
        setContent {
            PetCommunityTheme {
                val lifecycleOwner = LocalLifecycleOwner.current
                val intent = Intent(this, MyService::class.java)

                val coroutine = rememberCoroutineScope()
                val context = LocalContext.current
                lifecycleOwner.lifecycle.addObserver(MyObserver(coroutine, context, startService = {
                    startService(intent)
                }))
                val rootNavController = rememberNavController()
                RootNavigation(rootNavController, viewModel.isFirst)
            }
        }
    }
}

class MyObserver(
    val coroutine: CoroutineScope,
    val context: Context,
    val startService: () -> Unit
) : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
    }
    override fun onDestroy(owner: LifecycleOwner) {


//        GlobalScope.launch(Dispatchers.IO) {
//           // startService()
//            repeat(1000){
//                Log.d("XXX",it.toString())
//                Toast.makeText(context,it.toString(),Toast.LENGTH_SHORT).show()
//                delay(2000)
//            }
//
//        }
        super.onDestroy(owner)

    }


    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)

    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)

    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)

    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
    }
}

class SplashViewModel @Inject constructor(dataStoreManager: DataStoreManager) : ViewModel() {
    var isFirst = true

    init {
        isFirst = dataStoreManager.getRunFirst()
    }
}

