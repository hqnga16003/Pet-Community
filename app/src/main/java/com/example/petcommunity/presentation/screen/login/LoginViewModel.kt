package com.example.petcommunity.presentation.screen.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcommunity.data.Resource
import com.example.petcommunity.data.AuthRepository
import com.example.petcommunity.data.local_data.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val _loginEvent = Channel<LoginUiEvent>()
    val loginEvent = _loginEvent.receiveAsFlow()

    private val _loginFlowState = MutableStateFlow(LoginUiState())
    val loginFlowState = _loginFlowState.asStateFlow()


    init {
        viewModelScope.launch() {
            _loginFlowState.emit(
                _loginFlowState.value.copy(
                    isStayLoggedIn = dataStoreManager.getStayLogged(),
                    email = dataStoreManager.getEmail(),
                    password = dataStoreManager.getPassword()
                )
            )
        }
    }

    fun onEvent(event: LoginUiEvent) {
        viewModelScope.launch {

            when (event) {
                is LoginUiEvent.EmailChanged -> {
                    _loginFlowState.emit(_loginFlowState.value.copy(email = event.input))


                }

                is LoginUiEvent.PasswordChanged -> {
                    _loginFlowState.emit(_loginFlowState.value.copy(password = event.input))

                }

                is LoginUiEvent.StayLoggedChanged -> {
                    dataStoreManager.setStayLogged(event.isCheck)
                    _loginFlowState.emit(_loginFlowState.value.copy(isStayLoggedIn = event.isCheck))

                }

                is LoginUiEvent.Login -> {
                    login(_loginFlowState.value.email, _loginFlowState.value.password)
                }

                is LoginUiEvent.NavigationSignUp -> {
                    _loginEvent.send(LoginUiEvent.NavigationSignUp)


                }

                is LoginUiEvent.NavigationForgotPassword -> {
                    _loginEvent.send(LoginUiEvent.NavigationForgotPassword)

                }

                is LoginUiEvent.NavigationHome -> {
                    _loginEvent.send(LoginUiEvent.NavigationHome)
                    if (_loginFlowState.value.isStayLoggedIn == true){
                        dataStoreManager.setEmail(_loginFlowState.value.email)
                        dataStoreManager.setPassword(_loginFlowState.value.password)
                    }else{
                        dataStoreManager.removeAccount()
                    }
                }

                is LoginUiEvent.ErrorEvent -> {
                    _loginEvent.send(LoginUiEvent.ErrorEvent(event.mess))

                }
            }
        }
    }

    private suspend fun login(email: String, password: String) {
        authRepository.login(email, password).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _loginFlowState.emit(_loginFlowState.value.copy(loading = false))
                    onEvent(LoginUiEvent.NavigationHome)
                }

                is Resource.Error -> {
                    _loginFlowState.emit(_loginFlowState.value.copy(loading = false))

                    when (result.message.toString()) {
                        "The supplied auth credential is incorrect, malformed or has expired." -> {
                            onEvent(LoginUiEvent.ErrorEvent("Tai khoan khong dung"))
                        }

                        "The email address is badly formatted." -> {
                            onEvent(LoginUiEvent.ErrorEvent("Tai khoan la email"))
                        }

                        "Given String is empty or null" -> {
                            onEvent(LoginUiEvent.ErrorEvent("Khong duoc nhap trong"))
                        }

                        "A network error (such as timeout, interrupted connection or unreachable host) has occurred." -> {
                            onEvent(LoginUiEvent.ErrorEvent("Mat ket noi"))
                        }
                    }
                }

                is Resource.Loading -> {
                    _loginFlowState.emit(_loginFlowState.value.copy(loading = true))
                }
            }

        }
    }

}


data class LoginUiState(
    var email: String = "",
    var password: String = "",
    var loading: Boolean = false,
    var isStayLoggedIn: Boolean? = null
)

sealed class LoginUiEvent {
    data class EmailChanged(val input: String) : LoginUiEvent()
    data class PasswordChanged(val input: String) : LoginUiEvent()
    data class StayLoggedChanged(val isCheck: Boolean) : LoginUiEvent()
    data object Login : LoginUiEvent()
    data object NavigationHome : LoginUiEvent()
    data object NavigationForgotPassword : LoginUiEvent()
    data object NavigationSignUp : LoginUiEvent()
    data class ErrorEvent(val mess: String) : LoginUiEvent()
}


