package com.example.qwizz.data.model

data class Qwizzz(
    val id: String = "",
    val topic: String = "",
    val title: String = "",
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
