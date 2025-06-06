package com.example.qwizz


sealed  class Screen (val route: String) {
    object loginScreen : Screen("login_screen")
    object registerScreen : Screen("register_screen")
    object mainMenu : Screen("main_menu")
    object StatsMenu : Screen("stats_menu")
    object inputQuestion : Screen("input_question")
    object SelectTopic : Screen("select_topic")
    object searchSelectQwizzz : Screen("search_select_qwizz")
    object initialDoQwizzz : Screen("initial_do_qwizz")
    object mainQwizzz : Screen("main_qwizz")
    object hasilAkhir : Screen("hasil_akhir")
    object reviewQwizzz : Screen("review_qwizz")
    object leaderboard : Screen("leaderboard")
    object editAvailableQwizzz : Screen("edit_available_qwizz")
    object editQwizzz : Screen("edit_qwizz")


    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

    fun withArgs(qwizzzJson: String): String {
        return "$route/$qwizzzJson"
    }
}