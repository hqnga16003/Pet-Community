package com.example.petcommunity.presentation.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcommunity.data.local_data.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val dataStoreManager: DataStoreManager) : ViewModel() {

    private val _splashEvent = Channel<Boolean>()
    val splashEvent = _splashEvent.receiveAsFlow()


    init {

        viewModelScope.launch {
            delay(2000)
            checkRunFirst()
        }
    }

    private suspend fun checkRunFirst() {

        dataStoreManager.getRunFirst().collect {
            if (it) {
                _splashEvent.send(true)
            } else {
                _splashEvent.send(false)
            }
        }

    }


}



