package com.example.qwizz.data.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val lastLogin: Long = 0,
    val mathscore: MutableList<Double> = mutableListOf(),
    val bahasascore: MutableList<Double> = mutableListOf(),
)
