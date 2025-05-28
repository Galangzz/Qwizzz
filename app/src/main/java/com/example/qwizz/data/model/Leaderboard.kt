package com.example.qwizz.data.model

data class Leaderboard (
    val qwizzzId: String = "",
    val leaderboard: List<UserStats> = emptyList()
)

data class UserStats(
    val username: String = "",
    val score: Double = 0.0
)
