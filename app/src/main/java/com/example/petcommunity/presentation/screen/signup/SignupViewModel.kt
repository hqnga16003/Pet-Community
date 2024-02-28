package com.example.petcommunity.presentation.screen.signup

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcommunity.data.AuthRepository
import com.example.petcommunity.data.FireBaseFireStoreRepository
import com.example.petcommunity.data.Resource
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val fireBaseFireStoreRepository: FireBaseFireStoreRepository
) : ViewModel() {
    private val _registerEvent = Channel<RegisterUiEvent>()
    val registerEvent = _registerEvent.receiveAsFlow()
    private val _registerFlowState = MutableStateFlow(RegisterUiState())
    val registerFlowState = _registerFlowState.asStateFlow()
    fun onEvent(event: RegisterUiEvent) {
        viewModelScope.launch {
            when (event) {
                is RegisterUiEvent.PhoneNumberChanged -> {
                    _registerFlowState.emit(_registerFlowState.value.copy(phoneNumber = event.input))
                    if (isValidPhoneNumber()) {
                        _registerFlowState.emit(_registerFlowState.value.copy(isErrorPhoneNumber = false))
                    }
                }
                is RegisterUiEvent.EmailChanged -> {
                    _registerFlowState.emit(_registerFlowState.value.copy(email = event.input))

                    if (_registerFlowState.value.errorEmail.isNotEmpty()) {
                        if (isValidEmail()) {
                            _registerFlowState.emit(
                                _registerFlowState.value.copy(
                                    isErrorEmail = false
                                )
                            )
                        } else {
                            _registerFlowState.emit(
                                _registerFlowState.value.copy(
                                    isErrorEmail = true, errorEmail = "Email is invalid!"
                                )
                            )

                        }
                    }
                }
                is RegisterUiEvent.PasswordChanged -> {
                    _registerFlowState.emit(_registerFlowState.value.copy(password = event.input))
                    if (_registerFlowState.value.errorPassword.isNotEmpty()) {
                        if (isValidPassword()) {
                            _registerFlowState.emit(
                                _registerFlowState.value.copy(
                                    isErrorPassword = false,
                                )
                            )
                        } else {
                            _registerFlowState.emit(
                                _registerFlowState.value.copy(
                                    isErrorPassword = true,
                                    errorPassword = "Password less than 6 char!"
                                )
                            )
                        }
                    }
                }
                is RegisterUiEvent.ConfirmPasswordChanged -> {
                    _registerFlowState.emit(_registerFlowState.value.copy(confirmPassword = event.input))
                    if (_registerFlowState.value.errorConfirmPassword.isNotEmpty()) {
                        if (isValidConfirmPassword()) {
                            _registerFlowState.emit(
                                _registerFlowState.value.copy(
                                    isErrorConfirmPassword = false
                                )
                            )
                        } else {
                            _registerFlowState.emit(
                                _registerFlowState.value.copy(
                                    isErrorConfirmPassword = true,
                                    errorConfirmPassword = "Password not matching!"
                                )
                            )

                        }
                    }
                }
                is RegisterUiEvent.AvatarUriChanged -> {
                    _registerFlowState.emit(_registerFlowState.value.copy(avatarUri = event.avatarUri))
                }
                is RegisterUiEvent.UserNameChanged -> {
                    _registerFlowState.emit(_registerFlowState.value.copy(userName = event.input))
                    if (_registerFlowState.value.errorUserName.isNotEmpty()) {

                        _registerFlowState.emit(
                            _registerFlowState.value.copy(
                                isErrorUserName = !isValidUserName()
                            )
                        )

                    }

                }
                is RegisterUiEvent.DateOfBirthChanged -> {
                    _registerFlowState.emit(
                        _registerFlowState.value.copy(
                            dateOfBirth = event.input,
                            isErrorDateOfBirth = false,
                            errorDateOfBirth = ""
                        )
                    )

                }
                is RegisterUiEvent.GenderChanged -> {
                    _registerFlowState.emit(
                        _registerFlowState.value.copy(
                            gender = event.input,
                            expandedDropMenuGender = false,
                            isErrorGender = false,
                            errorGender = ""
                        )
                    )


                }

                is RegisterUiEvent.AddressChanged -> {
                    _registerFlowState.emit(_registerFlowState.value.copy(address = event.input))
                    if (_registerFlowState.value.errorAddress.isNotEmpty()) {
                        _registerFlowState.emit(
                            _registerFlowState.value.copy(
                                isErrorAddress = !isValidAddress()
                            )
                        )
                    }
                }

                is RegisterUiEvent.OTPCodeChanged -> {
                    _registerFlowState.emit(_registerFlowState.value.copy(otpSMS = event.input))

                }

                is RegisterUiEvent.Finish -> {
                    val isValidUserName = isValidUserName()
                    val isValidDateOfBirth = isValidDateOfBirth()
                    val isValidGender = isValidGender()
                    val isValidAddress = isValidAddress()
                    _registerFlowState.emit(
                        _registerFlowState.value.copy(
                            isErrorUserName = !isValidUserName,
                            isErrorDateOfBirth = !isValidDateOfBirth,
                            isErrorGender = !isValidGender,
                            isErrorAddress = !isValidAddress,
                            errorUserName = if (!isValidUserName) "Please enter your full name" else "",
                            errorDateOfBirth = if (!isValidDateOfBirth) "Please enter your date of birth" else "",
                            errorGender = if (!isValidGender) "Please enter your gender" else "",
                            errorAddress = if (!isValidAddress) "Please enter your address" else "",
                        )
                    )

                    if (!_registerFlowState.value.isErrorUserName && !_registerFlowState.value.isErrorDateOfBirth && !_registerFlowState.value.isErrorGender && !_registerFlowState.value.isErrorAddress) {
                        sendSMS(event.context)
                        onEvent(RegisterUiEvent.StartTimeOutSendSMS)
                        _registerEvent.send(RegisterUiEvent.Finish(event.context))

                    }
                }

                is RegisterUiEvent.CloseDialogDatePicker -> {
                    _registerFlowState.emit(_registerFlowState.value.copy(openDialogDatePicker = false))
                }

                is RegisterUiEvent.OpenDialogDatePicker -> {
                    _registerFlowState.emit(_registerFlowState.value.copy(openDialogDatePicker = true))
                }

                is RegisterUiEvent.OnExpandedDropMenuGenderChange -> {
                    _registerFlowState.emit(_registerFlowState.value.copy(expandedDropMenuGender = event.input))

                }

                is RegisterUiEvent.NavigationBack -> {
                    _registerEvent.send(RegisterUiEvent.NavigationBack)

                }

                is RegisterUiEvent.NavigationLogin -> {

                    _registerEvent.send(RegisterUiEvent.NavigationLogin)
                }

                is RegisterUiEvent.ErrorEvent -> {

                }


                is RegisterUiEvent.Register -> {

                    val isEmptyPhoneNumber = _registerFlowState.value.phoneNumber.isEmpty()
                    val isEmptyEmail = _registerFlowState.value.email.isEmpty()
                    val isEmptyPassword = _registerFlowState.value.password.isEmpty()
                    val isEmptyConfirmPassword = _registerFlowState.value.confirmPassword.isEmpty()


                    val isValidEmail = isValidEmail()
                    val isValidPassword = isValidPassword()
                    val isValidConfirmPassword = isValidConfirmPassword()

                    _registerFlowState.emit(
                        _registerFlowState.value.copy(
                            isErrorEmail = !isValidEmail,
                            isErrorPassword = !isValidPassword,
                            isErrorConfirmPassword = !isValidConfirmPassword(),
                            isErrorPhoneNumber = !isValidPhoneNumber(),
                            errorPhoneNumber = if (isEmptyPhoneNumber) "Please enter your phone number" else "",
                            errorEmail = if (isEmptyEmail) "Please enter your email" else if (!isValidEmail) "Email is invalid!" else "",
                            errorPassword = if (isEmptyPassword) "Please enter your password" else if (!isValidPassword) "Password less than 6 char!" else "",
                            errorConfirmPassword = if (isEmptyConfirmPassword) "Please enter your confirm password" else if (!isValidConfirmPassword) "Password not matching!" else "",
                        )
                    )
                    if (!_registerFlowState.value.isErrorEmail && !_registerFlowState.value.isErrorPassword && !_registerFlowState.value.isErrorConfirmPassword && !_registerFlowState.value.isErrorPhoneNumber) {
                        _registerEvent.send(RegisterUiEvent.Register)

                    }
                }

                is RegisterUiEvent.Verify -> {
                    if (_registerFlowState.value.otpSMS.length==6){
                        verifySMS()
                    }else{
                        _registerEvent.send(RegisterUiEvent.ErrorEvent("OTP is invalid"))
                    }
                }

                is RegisterUiEvent.StartTimeOutSendSMS -> {
                    startTimeout()
                }

                is RegisterUiEvent.Resend -> {
                    setTimeout()
                    sendSMS(context = event.context)
                    startTimeout()
                }

            }
        }
    }
    private fun sendSMS(context: Context) {
        val options = PhoneAuthOptions.newBuilder()
            .setPhoneNumber("+84${_registerFlowState.value.phoneNumber}") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(context as Activity) // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.d("XXXX",e.message.toString())
                }


                override fun onCodeSent(
                    verificationId: String, token: PhoneAuthProvider.ForceResendingToken
                ) {
                    super.onCodeSent(verificationId, token)
                    viewModelScope.launch {
                        fireBaseFireStoreRepository.saveOtpSMS(
                            "+84${_registerFlowState.value.phoneNumber}", verificationId
                        )
                    }
                }
            }).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    private fun register() {
        viewModelScope.launch {
            authRepository.register(
                _registerFlowState.value.email,
                _registerFlowState.value.password,
                _registerFlowState.value.userName,
                _registerFlowState.value.avatarUri
            )
        }
    }

    private fun deleteAccountPhoneNumber() {
        viewModelScope.launch {
            authRepository.deleteAccountPhoneNumber()
        }
    }

    private fun saveUserInfo() {
        viewModelScope.launch {
            fireBaseFireStoreRepository.saveUser(
                _registerFlowState.value.email,
                _registerFlowState.value.password,
                _registerFlowState.value.userName,
                _registerFlowState.value.phoneNumber,
                _registerFlowState.value.dateOfBirth,
                _registerFlowState.value.gender,
                _registerFlowState.value.address
            )
        }
    }

    private  fun verifySMS() {
        viewModelScope.launch {
            authRepository.signInWithCredential(
                "+84${_registerFlowState.value.phoneNumber}", _registerFlowState.value.otpSMS
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _registerFlowState.emit(_registerFlowState.value.copy(loading = true))
                    }

                    is Resource.Success -> {
                        register()
                        deleteAccountPhoneNumber()
                        saveUserInfo()
                        _registerFlowState.emit(_registerFlowState.value.copy(loading = false))
                        _registerEvent.send(RegisterUiEvent.NavigationLogin)
                    }

                    is Resource.Error -> {
                        _registerFlowState.emit(_registerFlowState.value.copy(loading = false))
                        _registerEvent.send(RegisterUiEvent.ErrorEvent("OTP is incorrect"))
                    }
                }
            }
        }

    }


    private fun startTimeout() {
        viewModelScope.launch {
            repeat(60) {
                _registerFlowState.emit(_registerFlowState.value.copy(timeOut = _registerFlowState.value.timeOut - 1))
                delay(1000)
            }
        }
    }

    private  fun setTimeout() {
     viewModelScope.launch {
         _registerFlowState.emit(_registerFlowState.value.copy(timeOut = 60))
     }
    }


    private fun isValidEmail(): Boolean {
        val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        return _registerFlowState.value.email.matches(emailRegex)
    }

    private fun isValidPassword(): Boolean {
        return _registerFlowState.value.password.length >= 6
    }

    private fun isValidPhoneNumber(): Boolean {
        return _registerFlowState.value.phoneNumber.isNotEmpty()
    }

    private fun isValidConfirmPassword(): Boolean {
        return _registerFlowState.value.confirmPassword.isNotEmpty() && _registerFlowState.value.password == _registerFlowState.value.confirmPassword
    }

    private fun isValidUserName(): Boolean {
        return _registerFlowState.value.userName.isNotEmpty()
    }

    private fun isValidDateOfBirth(): Boolean {
        return _registerFlowState.value.dateOfBirth.isNotEmpty()
    }

    private fun isValidGender(): Boolean {
        return _registerFlowState.value.gender.isNotEmpty()
    }

    private fun isValidAddress(): Boolean {
        return _registerFlowState.value.address.isNotEmpty()
    }
}


data class RegisterUiState(
    var phoneNumber: String = "773346306",
    var isErrorPhoneNumber: Boolean = false,
    var errorPhoneNumber: String = "",
    var email: String = "hqnga1604@gmail.com",
    var isErrorEmail: Boolean = false,
    var errorEmail: String = "",
    var password: String = "123456",
    var isErrorPassword: Boolean = false,
    var errorPassword: String = "",
    var confirmPassword: String = "123456",
    var isErrorConfirmPassword: Boolean = false,
    var errorConfirmPassword: String = "",
    var avatarUri: Uri? = null,
    var userName: String = "Hoang Quang Nga",
    var isErrorUserName: Boolean = false,
    var errorUserName: String = "",
    var dateOfBirth: String = "16-03-2002",
    var isErrorDateOfBirth: Boolean = false,
    var errorDateOfBirth: String = "",
    var gender: String = "Male",
    var isErrorGender: Boolean = false,
    var errorGender: String = "",
    var address: String = "Ha Noi",
    var isErrorAddress: Boolean = false,
    var errorAddress: String = "",
    var otpSMS: String = "",
    var timeOut: Int = 60,
    var loading: Boolean = false,
    var openDialogDatePicker: Boolean = false,
    var expandedDropMenuGender: Boolean = false,
    var storedVerificationId: String = ""
)

sealed class RegisterUiEvent {
    data class PhoneNumberChanged(val input: String) : RegisterUiEvent()

    data class EmailChanged(val input: String) : RegisterUiEvent()
    data class PasswordChanged(val input: String) : RegisterUiEvent()
    data class ConfirmPasswordChanged(val input: String) : RegisterUiEvent()
    data class AvatarUriChanged(val avatarUri: Uri) : RegisterUiEvent()

    data class UserNameChanged(val input: String) : RegisterUiEvent()
    data class DateOfBirthChanged(val input: String) : RegisterUiEvent()
    data class GenderChanged(val input: String) : RegisterUiEvent()
    data class AddressChanged(val input: String) : RegisterUiEvent()
    data class OTPCodeChanged(val input: String) : RegisterUiEvent()

    data class OnExpandedDropMenuGenderChange(val input: Boolean) : RegisterUiEvent()

    data object Register : RegisterUiEvent()
    data class Verify(val context: Context) : RegisterUiEvent()

    data class Finish(val context: Context) : RegisterUiEvent()
    data object CloseDialogDatePicker : RegisterUiEvent()
    data object OpenDialogDatePicker : RegisterUiEvent()

    data object NavigationBack : RegisterUiEvent()
    data object NavigationLogin : RegisterUiEvent()

    data object StartTimeOutSendSMS : RegisterUiEvent()

    data class Resend(val context: Context) : RegisterUiEvent()

    data class ErrorEvent(val mess: String) : RegisterUiEvent()
}
