package com.example.petcommunity.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.petcommunity.presentation.onboarding.OnboardingScreen
import com.example.petcommunity.presentation.screen.home.HomeScreen
import com.example.petcommunity.presentation.screen.home.HomeViewModel
import com.example.petcommunity.presentation.screen.login.LoginScreen
import com.example.petcommunity.presentation.screen.signup.SignupScreen

 @Composable
fun RootNavigation(rootNavController: NavHostController, isFirst: Boolean) {

    val startDestination = if(isFirst) "onboarding" else "main"
    NavHost(rootNavController, startDestination = startDestination) {

        composable("onboarding") {
            OnboardingScreen(navigateLogin = {
                rootNavController.navigate("login")

            }, navigateRegister = {
                rootNavController.navigate("register")
            })
        }

        composable("login") {
            LoginScreen(navigateSignUp = {
                rootNavController.navigate("register")

            }, navigateForgotPassword = {

            }, navigateHome = {
                rootNavController.navigate("main"){
                    popUpTo("login") {
                        inclusive = true
                    }
                }
            })
        }

        composable("register") {
            SignupScreen(navigationLogin = {
                rootNavController.navigate("login"){
                    popUpTo("register") {
                        inclusive = false
                    }
                }
            })
        }
        composable("main") {
            MainNavigation()
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainNavigation() {
    val mainNavController = rememberNavController()
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { dest ->
                        dest.route == "home"
                    } == true,
                    label = {
                        Text(text = "Home")
                    },
                    icon = {
                        Icon(
                            imageVector = if (currentDestination?.hierarchy?.any { dest ->
                                    dest.route == "home"
                                } == true) {
                                items[0].selectedIcon
                            } else items[0].unselectedIcon,
                            contentDescription = ""
                        )
                    },
                    onClick = {
                        mainNavController.navigate(items[0].title.lowercase()) {
                            popUpTo(mainNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )

                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { dest ->
                        dest.route == "chat"
                    } == true,
                    label = {
                        Text(text = "Chat")
                    },
                    icon = {
                        Icon(
                            imageVector = if (currentDestination?.hierarchy?.any { dest ->
                                    dest.route == "chat"
                                } == true) {
                                items[1].selectedIcon
                            } else items[1].unselectedIcon,
                            contentDescription = ""
                        )
                    },
                    onClick = {
                        mainNavController.navigate(items[1].title.lowercase()) {
                            popUpTo(mainNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )

            }
        }
    ) {
        NavHost(mainNavController, startDestination = "home") {
            composable("home") {
                HomeNavHost()
            }
            composable("chat") {
                ChatNavHost()
            }
            composable("settings") {
                SettingsNavHost()
            }
        }
    }
}

data class BottomNavigationItem(
    val title: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
)

val items = listOf(
    BottomNavigationItem(
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    BottomNavigationItem(
        title = "Chat",
        selectedIcon = Icons.Filled.Email,
        unselectedIcon = Icons.Outlined.Email,

        ),
    BottomNavigationItem(
        title = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,

        ),
)

@Composable
fun HomeNavHost() {
    val homeNavController = rememberNavController()
    NavHost(homeNavController, startDestination = "home") {
        composable("home") {
            HomeScreen(homeViewModel = hiltViewModel<HomeViewModel>())
        }
    }
}

@Composable
fun ChatNavHost() {
    val chatNavController = rememberNavController()
    NavHost(chatNavController, startDestination = "chat1") {
        for (i in 1..10) {
            composable("chat$i") {
                GenericScreen(
                    text = "Chat $i",
                    onNextClick = {
                        if (i < 10) {
                            chatNavController.navigate("chat${i + 1}")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SettingsNavHost() {
    val settingsNavController = rememberNavController()
    NavHost(settingsNavController, startDestination = "settings1") {
        for (i in 1..10) {
            composable("settings$i") {
                GenericScreen(
                    text = "Settings $i",
                    onNextClick = {
                        if (i < 10) {
                            settingsNavController.navigate("settings${i + 1}")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun GenericScreen(
    text: String,
    onNextClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = text)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onNextClick) {
            Text("Next")
        }
    }
}

