package com.feature.login//package com.feature.login
//
//import android.widget.Toast
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.outlined.Lock
//import androidx.compose.material.icons.outlined.Person
//import androidx.compose.material3.Checkbox
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.Divider
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.ExperimentalComposeUiApi
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalLifecycleOwner
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.Lifecycle
//import androidx.lifecycle.repeatOnLifecycle
//
//@Composable
//fun LoginScreen(
//    navigateSignUp: () -> Unit,
//    navigateForgotPassword: () -> Unit,
//    navigateHome: () -> Unit
//) {
//
//    val loginViewModel = hiltViewModel<LoginViewModel>()
//    val uiState = loginViewModel.loginFlowState.collectAsState()
//    val context = LocalContext.current
//    val lifecycleOwner = LocalLifecycleOwner.current
//
//    LaunchedEffect(lifecycleOwner) {
//        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//            loginViewModel.loginEvent.collect { event ->
//                when (event) {
//                    is LoginUiEvent.NavigationHome -> {
//                        navigateHome()
//                    }
//
//                    is LoginUiEvent.NavigationSignUp -> {
//                        navigateSignUp()
//
//                    }
//
//                    is LoginUiEvent.NavigationForgotPassword -> {
//                        navigateForgotPassword()
//                    }
//
//                    is LoginUiEvent.ErrorEvent -> {
//                        Toast.makeText(context, event.mess, Toast.LENGTH_LONG).show()
//                    }
//
//                    else -> {
//
//                    }
//                }
//            }
//        }
//    }
//
//    Scaffold { paddingValues ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues = paddingValues), contentAlignment = Alignment.Center
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.Cyan)
//                    .padding(
//                        top = (LocalConfiguration.current.screenWidthDp * 0.075).dp,
//                        start = (LocalConfiguration.current.screenWidthDp * 0.075).dp
//                    )
//                    .align(Alignment.TopCenter)
//
//            ) {
//                TextTile(title = "Hello,\nWelcome back!")
//            }
//
//
//            Column(
//                modifier = Modifier
//                    .clip(RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp))
//                    .fillMaxWidth()
//                    .height((LocalConfiguration.current.screenHeightDp * 0.8).dp)
//                    .background(Color.White)
//                    .align(Alignment.BottomCenter)
//                    .padding((LocalConfiguration.current.screenWidthDp * 0.075).dp),
//                verticalArrangement = Arrangement.spacedBy(10.dp),
//            ) {
//
//                TextSubTitle(title = "Login with social media", color = Color.Blue)
//
//                LoginSocialMedia(loginFB = {}, loginGG = {})
//                Spacer(modifier = Modifier.height(10.dp))
//                DividerLogin()
//
//                TextFieldInput(
//                    value = uiState.value.email,
//                    modifier = Modifier.fillMaxWidth(),
//                    placeholder = "Username",
//                    leadingIcon = Icons.Outlined.Person,
//                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
//                ) {
//                    loginViewModel.onEvent(LoginUiEvent.EmailChanged(it))
//                }
//
//                TextFieldInput(
//                    value = uiState.value.password,
//                    modifier = Modifier.fillMaxWidth(),
//                    placeholder = "Password",
//                    leadingIcon = Icons.Outlined.Lock,
//                    keyboardOptions = KeyboardOptions(
//                        keyboardType = KeyboardType.Password,
//                        imeAction = ImeAction.Done
//                    ),
//                    isHide = true,
//                    keyboardActions = KeyboardActions(onDone = {
//                        loginViewModel.onEvent(LoginUiEvent.Login)
//                    })
//                ) {
//                    loginViewModel.onEvent(LoginUiEvent.PasswordChanged(it))
//                }
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.Center
//                    ) {
//                        Checkbox(
//                            checked = uiState.value.isStayLoggedIn ?: false,
//                            onCheckedChange = {
//                                loginViewModel.onEvent(LoginUiEvent.StayLoggedChanged(it))
//                            })
//                        TextSubTitle(title = "Stay logged in", color = Color.Cyan)
//                    }
//                    MyTextButton(title = "Forgot password?", color = Color.Cyan) {
//
//                    }
//
//                }
//
//
//                Spacer(modifier = Modifier.height(10.dp))
//                ButtonAuth(title = "Login", modifier = Modifier.fillMaxWidth()) {
//
//                    loginViewModel.onEvent(LoginUiEvent.Login)
//                }
//
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    Text(
//                        text = "Not a remember? ",
//                        style = TextStyle(color = Color.Cyan, fontSize = 16.sp)
//                    )
//                    Text(
//                        text = "Register",
//                        style = TextStyle(
//                            color = Color.Cyan,
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Bold
//                        ),
//                        modifier = Modifier.clickable {
//                            loginViewModel.onEvent(LoginUiEvent.NavigationSignUp)
//                        }
//                    )
//                }
//
//            }
//            Image(
//                painter = painterResource(id = R.drawable.corgi_hello_logo),
//                contentDescription = "",
//                contentScale = ContentScale.Fit,
//                modifier = Modifier
//                    .padding(
//                        top = (LocalConfiguration.current.screenWidthDp * 0.16).dp,
//                        end = (LocalConfiguration.current.screenWidthDp * 0.075).dp
//                    )
//                    .width((LocalConfiguration.current.screenWidthDp * 0.3).dp)
//                    .height((LocalConfiguration.current.screenWidthDp * 0.3).dp)
//                    .align(Alignment.TopEnd)
//            )
//
//            if (uiState.value.loading) {
//                CircularProgressIndicator()
//            }
//        }
//
//    }
//}
//
//
//@Composable
//private fun LoginSocialMedia(loginFB: () -> Unit, loginGG: () -> Unit) {
//    Row(horizontalArrangement = Arrangement.SpaceBetween) {
//        Image(
//            painter = painterResource(id = R.drawable.btn_login_fb),
//            contentDescription = "",
//            modifier = Modifier
//                .weight(2f)
//                .clickable { loginFB() })
//        Spacer(modifier = Modifier.weight(1f))
//        Image(
//            painter = painterResource(id = R.drawable.btn_login_gg),
//            contentDescription = "",
//            modifier = Modifier
//                .weight(2f)
//                .clickable { loginGG() })
//    }
//}
//
//@Composable
//private fun DividerLogin() {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.Center
//    ) {
//        Divider(
//            color = Color.Cyan, thickness = 1.dp, modifier = Modifier
//                .weight(1f)
//                .padding(start = 30.dp, end = 10.dp)
//        )
//        TextSubTitle(title = "or", color = Color.Cyan)
//        Divider(
//            color = Color.Cyan, thickness = 1.dp, modifier = Modifier
//                .weight(1f)
//                .padding(end = 30.dp, start = 10.dp)
//        )
//
//    }
//}