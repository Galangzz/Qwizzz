package com.example.qwizz.data.model

import com.google.firebase.Timestamp

data class Qwizzz(
    val id: String = "",
    val name: String = "",
    val topic: String = "",
    val title: String = "",
    val createdAt: Timestamp? = null,
    val timeQuiz: Int = 0,
    val question: List<QuizQuestion> = emptyList()
)

data class QuizQuestion(
    val questionText: String = "",
    val options: List<AnswerOption> = emptyList()
)

data class AnswerOption(
    val text: String = "",
    val isCorrect: Boolean = false
)
