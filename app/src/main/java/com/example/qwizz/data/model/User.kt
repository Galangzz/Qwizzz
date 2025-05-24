package com.example.qwizz.data.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val lastLogin: Long = 0,
    val mathscore: MutableList<Int> = mutableListOf(),
    val bahasascore: MutableList<Int> = mutableListOf(),
)
