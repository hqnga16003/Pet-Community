package com.example.petcommunity.presentation.screen.signup

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import com.example.petcommunity.R
import com.example.petcommunity.presentation.wigdet.ButtonAuth
import com.example.petcommunity.presentation.wigdet.ButtonAuthOutline
import com.example.petcommunity.presentation.wigdet.DropdownMenuGender
import com.example.petcommunity.presentation.wigdet.TextFieldClick
import com.example.petcommunity.presentation.wigdet.TextFieldInput
import com.example.petcommunity.presentation.wigdet.TextFieldInputNumber
import com.example.petcommunity.presentation.wigdet.TextSubTitleWithBold
import com.example.petcommunity.presentation.wigdet.TextTile
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SignupScreen(navigationLogin: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = {
        3
    }, initialPage = 0)
    val registerViewModel = hiltViewModel<SignupViewModel>()
    val uiState = registerViewModel.registerFlowState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    val context = LocalContext.current

    val pickMedia =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                registerViewModel.onEvent(RegisterUiEvent.AvatarUriChanged(uri!!))
            }

        )

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            registerViewModel.registerEvent.collect { event ->
                when (event) {
                    is RegisterUiEvent.NavigationBack -> {
                        pagerState.scrollToPage(pagerState.currentPage - 1)

                    }

                    is RegisterUiEvent.Register -> {
                        pagerState.scrollToPage(1)
                    }

                    is RegisterUiEvent.Finish -> {
                        pagerState.scrollToPage(2)

                    }

                    is RegisterUiEvent.NavigationLogin -> {
                        navigationLogin()
                    }


                    is RegisterUiEvent.ErrorEvent ->{
                        Toast.makeText(context,event.mess,Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }
        }
    }


    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Cyan)
                    .padding(
                        top = (LocalConfiguration.current.screenWidthDp * 0.075).dp,
                        start = (LocalConfiguration.current.screenWidthDp * 0.075).dp
                    )
                    .align(Alignment.TopCenter)

            ) {
                TextTile(
                    title = when (pagerState.currentPage) {
                        0 -> "Hello,\nFill in to register!"
                        1 -> "Tell us\nabout your self!"
                        2 -> "Verify\nyour account"
                        else -> ""
                    }
                )
            }

            HorizontalPager(state = pagerState, userScrollEnabled = false) {

                when (pagerState.currentPage) {
                    0 -> {
                        PageFirst(uiState, registerViewModel)

                    }

                    1 -> {
                        PageSecond(uiState, registerViewModel, context)
                    }

                    2 -> {
                        PageThird(uiState, registerViewModel, context)
                    }
                }
            }


            if (pagerState.currentPage != 1) {
                Image(
                    painter = painterResource(id = R.drawable.corgi_hello_logo),
                    contentDescription = "",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .padding(
                            top = (LocalConfiguration.current.screenWidthDp * 0.16).dp,
                            end = (LocalConfiguration.current.screenWidthDp * 0.075).dp
                        )
                        .width((LocalConfiguration.current.screenWidthDp * 0.3).dp)
                        .height((LocalConfiguration.current.screenWidthDp * 0.3).dp)
                        .align(Alignment.TopEnd)
                )
            } else {
                if (uiState.value.avatarUri == null) {
                    Box(
                        modifier = Modifier
                            .padding(
                                top = (LocalConfiguration.current.screenWidthDp * 0.20).dp,
                                end = (LocalConfiguration.current.screenWidthDp * 0.075).dp
                            )
                            .clip(CircleShape)
                            .background(Color.LightGray)
                            .size(150.dp)
                            .align(Alignment.TopEnd)
                            .clickable {
                                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

                            }, contentAlignment = Alignment.Center

                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.add_avata),
                            contentDescription = ""
                        )
                    }
                } else {
                    AsyncImage(
                        model = uiState.value.avatarUri,
                        modifier = Modifier
                            .padding(
                                top = (LocalConfiguration.current.screenWidthDp * 0.20).dp,
                                end = (LocalConfiguration.current.screenWidthDp * 0.075).dp
                            )
                            .clip(CircleShape)
                            .size(150.dp)
                            .align(Alignment.TopEnd)
                            .clickable {
                                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

                            },
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                    )
                }
            }

            if (uiState.value.loading) {
                CircularProgressIndicator()
            }
        }
    }

}


@Composable
private fun PageFirst(
    uiState: State<RegisterUiState>,
    registerViewModel: SignupViewModel,
) {

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp))
                .fillMaxWidth()
                .height((LocalConfiguration.current.screenHeightDp * 0.8).dp)
                .background(Color.White)
                .align(Alignment.BottomCenter)
                .padding((LocalConfiguration.current.screenWidthDp * 0.075).dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
        ) {

            TextFieldInputNumber(value = uiState.value.phoneNumber,
                isError = uiState.value.isErrorPhoneNumber,
                errorText = uiState.value.errorPhoneNumber,
                placeholder = "210 1234 567",
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next, keyboardType = KeyboardType.Number
                ),
                onValueChange = { value ->
                    registerViewModel.onEvent(RegisterUiEvent.PhoneNumberChanged(value))
                })

            TextFieldInput(value = uiState.value.email,
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.value.isErrorEmail,
                placeholder = "Email",
                errorText = uiState.value.errorEmail,
                leadingIcon = Icons.Outlined.Email,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                onValueChange = { value ->
                    registerViewModel.onEvent(RegisterUiEvent.EmailChanged(value))
                })

            TextFieldInput(value = uiState.value.password,
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.value.isErrorPassword,
                errorText = uiState.value.errorPassword,
                placeholder = "Password",
                leadingIcon = Icons.Outlined.Lock,
                isHide = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                onValueChange = { value ->
                    registerViewModel.onEvent(RegisterUiEvent.PasswordChanged(value))
                })

            TextFieldInput(value = uiState.value.confirmPassword,
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Confirm Password",
                isError = uiState.value.isErrorConfirmPassword,
                errorText = uiState.value.errorConfirmPassword,
                leadingIcon = Icons.Outlined.Lock,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                ),
                isHide = true,
                onValueChange = { value ->
                    registerViewModel.onEvent(RegisterUiEvent.ConfirmPasswordChanged(value))

                })

            ButtonAuth(title = "REGISTER", modifier = Modifier.fillMaxWidth()) {
                registerViewModel.onEvent(RegisterUiEvent.Register)
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Have an account? ",
                    style = TextStyle(color = Color.Cyan, fontSize = 16.sp), modifier = Modifier.clickable {
                        registerViewModel.onEvent(RegisterUiEvent.NavigationLogin)
                    }
                )
                Text(text = "Login", style = TextStyle(
                    color = Color.Cyan, fontSize = 16.sp, fontWeight = FontWeight.Bold
                ), modifier = Modifier.clickable {
                    registerViewModel.onEvent(RegisterUiEvent.NavigationLogin)
                })
            }
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PageSecond(
    uiState: State<RegisterUiState>,
    registerViewModel: SignupViewModel,
    context: Context
) {

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp))
                .fillMaxWidth()
                .height((LocalConfiguration.current.screenHeightDp * 0.8).dp)
                .background(Color.White)
                .align(Alignment.BottomCenter)
                .padding((LocalConfiguration.current.screenWidthDp * 0.075).dp)
                .padding(top = (LocalConfiguration.current.screenWidthDp * 0.15).dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            TextFieldInput(value = uiState.value.userName,
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.value.isErrorUserName,
                placeholder = "Your full name",
                errorText = uiState.value.errorUserName,
                leadingIcon = Icons.Outlined.Person,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                onValueChange = { value ->
                    registerViewModel.onEvent(RegisterUiEvent.UserNameChanged(value))
                })
            Row(modifier = Modifier.fillMaxWidth()) {
                TextFieldClick(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            registerViewModel.onEvent(RegisterUiEvent.OpenDialogDatePicker)
                        },
                    value = uiState.value.dateOfBirth,
                    placeholder = "Date of birth",
                    Icons.Outlined.Email,
                    isError = uiState.value.isErrorDateOfBirth,
                    errorText = uiState.value.errorDateOfBirth
                )

                Spacer(modifier = Modifier.width(10.dp))
                DropdownMenuGender(
                    modifier = Modifier.weight(1f),
                    expanded = uiState.value.expandedDropMenuGender,
                    isError = uiState.value.isErrorGender,
                    errorText = uiState.value.errorGender,
                    onExpandedChange = {
                        registerViewModel.onEvent(
                            RegisterUiEvent.OnExpandedDropMenuGenderChange(
                                it
                            )
                        )
                    }, value = uiState.value.gender,
                    onDismissRequest = {
                        registerViewModel.onEvent(
                            RegisterUiEvent.OnExpandedDropMenuGenderChange(
                                false
                            )
                        )
                    },
                    onClickDropdownMenuItem = {
                        registerViewModel.onEvent(
                            RegisterUiEvent.GenderChanged(
                                it
                            )
                        )
                    }
                )

            }

            TextFieldInput(value = uiState.value.address,
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.value.isErrorAddress,
                placeholder = "Address",
                errorText = uiState.value.errorUserName,
                leadingIcon = Icons.Outlined.LocationOn,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                onValueChange = { value ->
                    registerViewModel.onEvent(RegisterUiEvent.AddressChanged(value))
                })

            ButtonAuth(title = "FINISH", modifier = Modifier.fillMaxWidth()) {
                registerViewModel.onEvent(RegisterUiEvent.Finish(context))
            }

            ButtonAuthOutline(title = "BACK", modifier = Modifier.fillMaxWidth()) {

                registerViewModel.onEvent(RegisterUiEvent.NavigationBack)
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Have an account? ",
                    style = TextStyle(color = Color.Cyan, fontSize = 16.sp)
                )
                Text(text = "Login", style = TextStyle(
                    color = Color.Cyan, fontSize = 16.sp, fontWeight = FontWeight.Bold
                ), modifier = Modifier.clickable {})
            }
        }
    }

    if (uiState.value.openDialogDatePicker) {
        val datePickerState = rememberDatePickerState()
        MyDateTimePicker(onDismissRequest = {
            registerViewModel.onEvent(RegisterUiEvent.CloseDialogDatePicker)
        }, confirmButton = {
            registerViewModel.onEvent(RegisterUiEvent.CloseDialogDatePicker)
            if (datePickerState.selectedDateMillis != null) {
                registerViewModel.onEvent(
                    RegisterUiEvent.DateOfBirthChanged(
                        Tools.convertLongToTime(
                            datePickerState.selectedDateMillis!!
                        )
                    )
                )
            }
        }, datePickerState)

    }
}

@Composable
private fun PageThird(
    uiState: State<RegisterUiState>,
    registerViewModel: SignupViewModel,
    context: Context
) {

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp))
                .fillMaxWidth()
                .height((LocalConfiguration.current.screenHeightDp * 0.8).dp)
                .background(Color.White)
                .align(Alignment.BottomCenter)
                .padding((LocalConfiguration.current.screenWidthDp * 0.075).dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TextSubTitleWithBold(title = "Enter OTP code to verify")



            BasicTextField(value = uiState.value.otpSMS, onValueChange = { value ->
                if (value.length <= 6) {
                    registerViewModel.onEvent(RegisterUiEvent.OTPCodeChanged(value))
                }
            }, keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                    repeat(6) { index ->
                        val number = when {
                            index >= uiState.value.otpSMS.length -> ""
                            else -> uiState.value.otpSMS[index]
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = number.toString())
                            Box(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(2.dp)
                                    .background(Color.Black)
                            )
                        }

                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't receive? ",
                    style = TextStyle(color = Color.Cyan, fontSize = 16.sp)
                )
                Text(
                    text = "Resend",
                    style = TextStyle(
                        color = if (uiState.value.timeOut == 0) Color.Green else Color.LightGray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.clickable(enabled = uiState.value.timeOut < 50, onClick = {

                        registerViewModel.onEvent(RegisterUiEvent.Resend(context))

                    })
                )
                Text(
                    text = if (uiState.value.timeOut != 0) " (${uiState.value.timeOut})" else "",
                    style = TextStyle(color = Color.Cyan, fontSize = 16.sp)
                )
            }



            ButtonAuth(title = "VERIFY", modifier = Modifier.fillMaxWidth()) {
                registerViewModel.onEvent(RegisterUiEvent.Verify(context))
            }

            ButtonAuthOutline(title = "BACK", modifier = Modifier.fillMaxWidth()) {
                registerViewModel.onEvent(RegisterUiEvent.NavigationBack)

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDateTimePicker(
    onDismissRequest: () -> Unit, confirmButton: () -> Unit, datePickerState: DatePickerState
) {

    DatePickerDialog(onDismissRequest = onDismissRequest, confirmButton = {
        TextButton(onClick = confirmButton) {

            Text(text = "Oke")
        }
    }) {
        DatePicker(state = datePickerState)
    }
}


private class Tools {
    companion object {
        fun convertLongToTime(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            return format.format(date)
        }
    }
}
