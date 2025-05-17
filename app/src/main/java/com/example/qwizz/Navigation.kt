package com.example.qwizz

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.qwizz.ui.auth.LoginScreen
import com.example.qwizz.ui.auth.RegisterScreen
import com.example.qwizz.ui.menu.MainMenu
import com.example.qwizz.ui.menu.StatsMenu

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.loginScreen.route) {
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

    }
}