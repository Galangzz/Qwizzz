package com.example.qwizz.viewmodel.makeqwizz

import android.util.Log
import androidx.compose.runtime.currentRecomposeScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qwizz.data.control.QwizzControl
import com.example.qwizz.data.model.AnswerOption
import com.example.qwizz.data.model.LeaderboardUser
import com.example.qwizz.data.model.Qwizzz
import kotlinx.coroutines.delay
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

    private val _leaderboard = MutableStateFlow<List<LeaderboardUser>>(emptyList())
    val leaderboard: StateFlow<List<LeaderboardUser>> = _leaderboard

    private val _currentQwizzz = MutableStateFlow<Qwizzz?>(null)
    val currentQwizzz: StateFlow<Qwizzz?> = _currentQwizzz

    private val _score = MutableStateFlow<Double>(0.0)
    val score: StateFlow<Double> = _score

    private val _doQwizzzState = MutableStateFlow<DoQwizzzState>(DoQwizzzState.Initial)
    val doQwizzzState: StateFlow<DoQwizzzState> = _doQwizzzState

    init {
        Log.d("DoQwizzzViewModel", "ViewModel initialized")
        observeCurrentQwizzz()
    }

    fun observeCurrentQwizzz() {
        viewModelScope.launch {
            val id = qwizzControl.getIdQwizzz(currentQwizzz.value)
            if (id.isNotEmpty()){
                qwizzControl.observeCurrentQwizzz(id)
                    .collect { qwizzz ->
//                        _currentQwizzz.value = qwizzz
                        _leaderboard.value = qwizzz.leaderboard.sortedByDescending { it.score }
                        Log.d("DoQwizzzViewModel", "Current qwizzz updated: $qwizzz")
                        Log.d("DoQwizzzViewModel", "Leaderboard: ${leaderboard.value.size}")
                    }
            }
        }
    }

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
                _currentQwizzz.value = qwizzz
                _doQwizzzState.value = DoQwizzzState.QwizzzSaved
                Log.d("DoQwizzzViewModel", "Current qwizzz updated successfully")
            } catch (e: Exception) {
                _doQwizzzState.value = DoQwizzzState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun countScore(userAnswer: List<AnswerOption>) {
        setAnswer(userAnswer)
        var score = 0.0
        val qwizzz = currentQwizzz.value
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
        score = BigDecimal(score).setScale(1, RoundingMode.HALF_UP).toDouble()
        _score.value = score
        Log.d("DoQwizzzViewModel", "Score: $score")
    }

    fun addScore(){
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
                qwizzControl.updateUserScore(score.value, scoreKey)
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

    fun updateLeaderboard(){
        viewModelScope.launch {
            try {
                val qwiz = currentQwizzz.value
                val idQwizzz = qwizzControl.getIdQwizzz(qwiz)
                qwizzControl.updateLeaderboard(idQwizzz, score.value)
                Log.d("DoQwizzzViewModel", "Leaderboard updated successfully")
            } catch (e: Exception) {
                Log.e("DoQwizzzViewModel", "Error updating leaderboard", e)
            }
        }
    }

//    fun getLeaderboard(){
//        viewModelScope.launch {
//            try {
//                val qwiz = currentQwizzz.value
//                val leaderboard = qwiz?.leaderboard
//                _leaderboard.value = leaderboard ?: emptyList()
//                Log.d("DoQwizzzViewModel", "Leaderboard: ${leaderboard?.size}")
//            } catch (e: Exception) {
//                Log.e("DoQwizzzViewModel", "Error getting leaderboard", e)
//                _leaderboard.value = emptyList()
//            }
//        }
//    }




}

sealed class DoQwizzzState{
    object Initial: DoQwizzzState()
    object Loading: DoQwizzzState()
    object QwizzzSaved: DoQwizzzState()
    object QwizzzNotSaved: DoQwizzzState()
    data class Error(val message: String): DoQwizzzState()
}
