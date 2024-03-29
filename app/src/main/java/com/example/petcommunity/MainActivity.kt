package com.example.petcommunity


import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.rememberNavController
import com.example.petcommunity.ui.theme.PetCommunityTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.example.petcommunity.data.local_data.DataStoreManager
import com.example.petcommunity.presentation.RootNavigation


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var viewModel: SplashViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            PetCommunityTheme {

                val rootNavController = rememberNavController()
                RootNavigation(rootNavController, viewModel.isFirst)
            }
        }
    }
}
class SplashViewModel @Inject constructor(dataStoreManager: DataStoreManager) : ViewModel() {
    var isFirst = true

    init {
        isFirst = dataStoreManager.getRunFirst()
    }
}
