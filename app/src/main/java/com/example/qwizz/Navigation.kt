package com.example.qwizz

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.qwizz.ui.auth.LoginScreen
import com.example.qwizz.ui.auth.RegisterScreen
import com.example.qwizz.ui.makeqwizz.SelectTopic
import com.example.qwizz.ui.menu.MainMenu
import com.example.qwizz.ui.menu.StatsMenu
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Navigation(){
    val navController = rememberNavController()

    val user = FirebaseAuth.getInstance().currentUser
    val startDestination = if (user != null) Screen.mainMenu.route else Screen.loginScreen.route


    NavHost(navController = navController, startDestination = startDestination) {
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

    }
}