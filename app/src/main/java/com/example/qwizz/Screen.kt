package com.example.qwizz

sealed  class Screen (val route: String) {
    object loginScreen : Screen("login_screen")
    object registerScreen : Screen("register_screen")
    object mainMenu : Screen("main_menu")
    object StatsMenu : Screen("stats_menu")
    object MakeQwizz : Screen("make_qwizz")
    object SelectTopic : Screen("select_topic")


}