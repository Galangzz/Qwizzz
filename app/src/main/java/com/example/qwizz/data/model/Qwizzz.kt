package com.example.qwizz.data.model

data class Qwizzz(
    val email: String = "",
    val username: String = "",
    val topic: String = "",
    val question: List<QuizQuestion> = emptyList()
)

data class QuizQuestion(
    val questionText: String,
    val options: List<AnswerOption>
)

data class AnswerOption(
    val text: String,
    val isCorrect: Boolean
)
