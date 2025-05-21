package com.example.qwizz

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.qwizz.ui.auth.LoginScreen
import com.example.qwizz.ui.auth.RegisterScreen
import com.example.qwizz.ui.doqwizz.SearchSelectQwizzz
import com.example.qwizz.ui.makeqwizz.InputQuestion
import com.example.qwizz.ui.makeqwizz.SelectTopic
import com.example.qwizz.ui.menu.MainMenu
import com.example.qwizz.ui.menu.StatsMenu
import com.example.qwizz.viewmodel.auth.AuthViewModel
import com.example.qwizz.viewmodel.auth.AuthViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun Navigation(){
    val navController = rememberNavController()
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(context))
    val (startDestination, setStartDestination) = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        Log.d("Navigation", "LaunchedEffect triggered")
        delay(1000)
        val userIsLoggedIn = authViewModel.getCurrentUser() && authViewModel.isRemembered()
        setStartDestination(if (userIsLoggedIn) Screen.mainMenu.route else Screen.loginScreen.route)
        Log.d("Navigation", "User: ${authViewModel.getCurrentUser()} and remember: ${authViewModel.isRemembered()}")
    }

    startDestination?.let { destination ->
        NavHost(navController = navController, startDestination = destination) {
            composable(Screen.loginScreen.route) {
                LoginScreen(navController = navController)
            }
            composable(Screen.registerScreen.route) {
                RegisterScreen(navController = navController)
            }
            composable(Screen.mainMenu.route){
                MainMenu(navController = navController)
            }
            composable(Screen.StatsMenu.route){
                StatsMenu(navController = navController)
            }
            composable(Screen.SelectTopic.route) {
                SelectTopic(navController = navController)
            }
            composable(Screen.searchSelectQwizzz.route){
                Log.d("Navigation", "searchSelectQwizzz")
                SearchSelectQwizzz(navController = navController)
            }
            composable(
                route = Screen.inputQuestion.route + "/{topic}/{title}",
                arguments = listOf(
                    navArgument("topic") {
                        type = NavType.StringType
                        defaultValue = ""
                        nullable = true
                    },
                    navArgument("title") {
                        type = NavType.StringType
                        defaultValue = ""
                        nullable = true
                    }
                )
            ) { backStackEntry ->
                val topic = backStackEntry.arguments?.getString("topic")
                val title = backStackEntry.arguments?.getString("title")
                InputQuestion(navController = navController, topic = topic.toString(), title = title.toString())
            }
        }
    } ?: run{
        Log.d("Navigation", "startDestination is null")
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }


}