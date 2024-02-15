package com.example.petcommunity.presentation.onboarding

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcommunity.data.local_data.DataStoreManager
import com.example.petcommunity.presentation.screen.login.LoginUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OnBoardingViewModel @Inject constructor(private val dataStoreManager: DataStoreManager) :
    ViewModel() {

    private val _onboardingEvent = Channel<OnboardingUiEvent>()
    val onboardingEvent = _onboardingEvent.receiveAsFlow()


    fun onEvent(event: OnboardingUiEvent) {
        viewModelScope.launch {
            when (event) {
                is OnboardingUiEvent.NavigationLogin -> {
                    _onboardingEvent.send(OnboardingUiEvent.NavigationLogin)
                }

                is OnboardingUiEvent.NavigationRegister -> {
                    _onboardingEvent.send(OnboardingUiEvent.NavigationRegister)
                }

            }
            setRunFirst()
        }
    }


    private fun setRunFirst() {
        viewModelScope.launch {
            dataStoreManager.setRunFirst()
        }
    }


}


sealed class OnboardingUiEvent {

    data object NavigationRegister : OnboardingUiEvent()
    data object NavigationLogin : OnboardingUiEvent()

}
