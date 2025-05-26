package com.example.qwizz.viewmodel.makeqwizz

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qwizz.data.control.QwizzControl
import com.example.qwizz.data.model.AnswerOption
import com.example.qwizz.data.model.Qwizzz
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

class DoQwizzzViewModel: ViewModel() {
    private val qwizzControl = QwizzControl()

    private val _quizList = MutableStateFlow<List<Qwizzz>>(emptyList())
    val quizList: StateFlow<List<Qwizzz>> = _quizList

    private val _stackAnswer = MutableStateFlow<List<AnswerOption>>(emptyList())
    val stackAnswer: StateFlow<List<AnswerOption>> = _stackAnswer

    private val _doQwizzzState = MutableStateFlow<DoQwizzzState>(DoQwizzzState.Initial)
    val doQwizzzState: StateFlow<DoQwizzzState> = _doQwizzzState


    fun fetchQuizzes() {
        Log.d("DoQwizzzViewModel", "Fetching quizzes...")
        viewModelScope.launch {
            _doQwizzzState.value = DoQwizzzState.Loading
            try {

                val result = qwizzControl.getQwizzz()
                _quizList.value = result

                if (result.isNotEmpty()) {
                    _doQwizzzState.value = DoQwizzzState.Initial
                    Log.d("DoQwizzzViewModel", "Fetched quizzes successfully: ${result.size}")
                } else {
                    _doQwizzzState.value = DoQwizzzState.Error("No quizzes found.")
                    Log.d("DoQwizzzViewModel", "No quizzes found.")
                }

            } catch (e: Exception) {

                _doQwizzzState.value = DoQwizzzState.Error(e.localizedMessage ?: "Unknown error")
                Log.e("DoQwizzzViewModel", "Error fetching quizzes: ${e.localizedMessage}", e)
            }
        }
    }

    fun updateCurrentQwizzz(qwizzz: Qwizzz) {
        viewModelScope.launch {
            try {
                _quizList.value = listOf(qwizzz)
                _doQwizzzState.value = DoQwizzzState.QwizzzSaved
                Log.d("DoQwizzzViewModel", "Qwizzz updated successfully: $qwizzz")
            } catch (e: Exception) {
                _doQwizzzState.value = DoQwizzzState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun countScore(userAnswer: List<AnswerOption>): Double {
        setAnswer(userAnswer)
        var score = 0.0
        val qwizzz = quizList.value.firstOrNull()
        val correctOptionUser = mutableListOf<String>()
        val correctOptionTexts = mutableListOf<String>()
        qwizzz?.question?.forEach { quizQuestion ->
            quizQuestion.options.forEach { answerOption ->
                if (answerOption.correct) {
                    correctOptionTexts.add(answerOption.text)
                }
            }
        }
        userAnswer.forEach { answerOption ->
            if (answerOption.correct) {
                correctOptionUser.add(answerOption.text)
            }
        }

        correctOptionUser.forEach { userAnswerText ->
            if (correctOptionTexts.contains(userAnswerText)) {
                score++
            }
        }
        score = (score / correctOptionTexts.size) * 100
//        score = String.format("%.2f", score).toDouble()
        score = BigDecimal(score).setScale(1, RoundingMode.HALF_UP).toDouble()
        Log.d("DoQwizzzViewModel", "Score: $score")

        return score
    }

    fun addScore(score: Double){
        val qwizzz = quizList.value.firstOrNull()
        val topic = qwizzz?.topic?: ""
        val scoreKey = if (topic == "Matematika"){
            "mathscore"
        } else if (topic == "Bahasa"){
            "bahasascore"
        } else {
            Log.d("DoQwizzzViewModel", "Invalid topic: $topic")
            topic
        }

        viewModelScope.launch {
            try {
                qwizzControl.updateUserScore(score, scoreKey)
                Log.d("DoQwizzzViewModel", "Score added successfully")
            } catch (e: Exception) {
                Log.e("DoQwizzzViewModel", "Error adding score", e)
            }
        }

    }

    fun setAnswer(answer: List<AnswerOption>){
        Log.d("DoQwizzzViewModel", "setAnswer: $answer")
        try{
            _stackAnswer.value = answer
            Log.d("DoQwizzzViewModel", "Answer set successfully")
            Log.d("DoQwizzzViewModel", "Answer: ${stackAnswer.value}")
        }catch (e: Exception){
            Log.e("DoQwizzzViewModel", "Error setting answer", e)
        }
    }




}

sealed class DoQwizzzState{
    object Initial: DoQwizzzState()
    object Loading: DoQwizzzState()
    object QwizzzSaved: DoQwizzzState()
    object QwizzzNotSaved: DoQwizzzState()
    data class Error(val message: String): DoQwizzzState()
}
